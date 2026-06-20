package com.cocode.tmsmeasurement.viewmodel

import com.cocode.tmsmeasurement.data.LegacyImporter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for [MeasurementController] — the plain-JVM core of the measurement
 * screen. Test doubles and data builders live in [MeasurementControllerFakes].
 */
@OptIn(ExperimentalCoroutinesApi::class)
class MeasurementControllerTest {

    @Test
    fun init_runsImport_writingLegacyRecordsToRepository() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val repo = FakeRepository()
        val flag = FakeFlag()
        val importer = LegacyImporter(FakeSource(listOf(legacyRecord("100"))), repo, flag)

        MeasurementController(repo, importer, TestScope(testScheduler), dispatcher, repo)
        advanceUntilIdle()

        assertEquals(listOf("100"), repo.inserted.map { it.id })
        assertTrue(flag.isDone())
    }

    @Test
    fun addMeasurement_persistsThroughRepository() = runTest {
        val repo = FakeRepository()
        val controller = controllerWith(repo, TestScope(testScheduler))

        controller.addMeasurement(entity("200"))
        advanceUntilIdle()

        assertEquals(listOf("200"), repo.inserted.map { it.id })
    }

    @Test
    fun deleteMeasurement_removesThroughRepository() = runTest {
        val repo = FakeRepository(seed = listOf(entity("300")))
        val controller = controllerWith(repo, TestScope(testScheduler))

        controller.deleteMeasurement("300")
        advanceUntilIdle()

        assertEquals(listOf("300"), repo.deleted)
    }

    @Test
    fun measurements_stateFlow_emitsRepositoryContents() = runTest {
        val scope = TestScope(testScheduler)
        val repo = FakeRepository(seed = listOf(entity("400")))
        val controller = controllerWith(repo, scope)

        // WhileSubscribed only collects upstream while observed; subscribe to activate it.
        val collector = scope.launch { controller.measurements.collect {} }
        advanceUntilIdle()

        assertEquals(listOf("400"), controller.measurements.value.map { it.id })
        collector.cancel()
    }

    @Test
    fun measurements_stateFlow_reflectsAdd() = runTest {
        val scope = TestScope(testScheduler)
        val repo = FakeRepository()
        val controller = controllerWith(repo, scope)

        val collector = scope.launch { controller.measurements.collect {} }
        controller.addMeasurement(entity("500"))
        advanceUntilIdle()

        assertEquals(listOf("500"), controller.measurements.value.map { it.id })
        collector.cancel()
    }

    @Test
    fun saveTreatment_persistsThroughRepository() = runTest {
        val repo = FakeRepository(seed = listOf(entity("m1")))
        val controller = controllerWith(repo, TestScope(testScheduler))

        controller.saveTreatment(treatment(measurementId = "m1"))
        advanceUntilIdle()

        assertEquals(listOf("m1"), repo.savedTreatments.map { it.measurementId })
    }

    @Test
    fun treatmentsFor_stateFlow_emitsRepositoryTreatments() = runTest {
        val scope = TestScope(testScheduler)
        val repo = FakeRepository(seed = listOf(entity("m1")))
        val controller = controllerWith(repo, scope)

        val flow = controller.treatmentsFor("m1")
        val collector = scope.launch { flow.collect {} }
        controller.saveTreatment(treatment(measurementId = "m1"))
        advanceUntilIdle()

        assertEquals(listOf("m1"), flow.value.map { it.measurementId })
        collector.cancel()
    }

    @Test
    fun treatmentsFor_onlyEmitsTreatmentsForRequestedMeasurement() = runTest {
        val scope = TestScope(testScheduler)
        val repo = FakeRepository(seed = listOf(entity("m1"), entity("m2")))
        val controller = controllerWith(repo, scope)

        val flow = controller.treatmentsFor("m1")
        val collector = scope.launch { flow.collect {} }
        controller.saveTreatment(treatment(measurementId = "m1"))
        controller.saveTreatment(treatment(measurementId = "m2"))
        advanceUntilIdle()

        assertEquals(listOf("m1"), flow.value.map { it.measurementId })
        collector.cancel()
    }

    @Test
    fun treatmentsFor_returnsSameInstanceForSameMeasurementId() = runTest {
        val controller = controllerWith(FakeRepository(), TestScope(testScheduler))

        val first = controller.treatmentsFor("m1")
        val second = controller.treatmentsFor("m1")

        assertSame(first, second)
    }

    @Test
    fun deleteTreatment_removesThroughRepository() = runTest {
        val scope = TestScope(testScheduler)
        val repo = FakeRepository(seed = listOf(entity("m1")))
        val controller = controllerWith(repo, scope)
        controller.saveTreatment(treatment(measurementId = "m1"))
        advanceUntilIdle()
        val saved = repo.savedTreatments.first()

        val flow = controller.treatmentsFor("m1")
        val collector = scope.launch { flow.collect {} }
        controller.deleteTreatment(saved)
        advanceUntilIdle()

        assertEquals(listOf(saved), repo.deletedTreatments)
        assertTrue(flow.value.isEmpty())
        collector.cancel()
    }

    @Test
    fun getTreatment_returnsSavedTreatment() = runTest {
        val repo = FakeRepository(seed = listOf(entity("m1")))
        val controller = controllerWith(repo, TestScope(testScheduler))
        controller.saveTreatment(treatment(measurementId = "m1"))
        advanceUntilIdle()
        val savedId = repo.savedTreatments.first().id

        val loaded = controller.getTreatment(savedId)

        assertEquals("m1", loaded?.measurementId)
    }

    @Test
    fun deletingMeasurement_removesItsTreatments() = runTest {
        val scope = TestScope(testScheduler)
        val repo = FakeRepository(seed = listOf(entity("m1"), entity("m2")))
        val controller = controllerWith(repo, scope)
        controller.saveTreatment(treatment(measurementId = "m1"))
        controller.saveTreatment(treatment(measurementId = "m2"))
        advanceUntilIdle()

        val m1Flow = controller.treatmentsFor("m1")
        val m2Flow = controller.treatmentsFor("m2")
        val collectors = listOf(
            scope.launch { m1Flow.collect {} },
            scope.launch { m2Flow.collect {} }
        )
        controller.deleteMeasurement("m1")
        advanceUntilIdle()

        assertTrue(m1Flow.value.isEmpty())
        assertEquals(listOf("m2"), m2Flow.value.map { it.measurementId })
        collectors.forEach { it.cancel() }
    }
}
