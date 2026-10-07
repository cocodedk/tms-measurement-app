// The Beam F3 calculator of the home page. Same formula as the English page and the app.
const form = document.getElementById("beamf3-form");
const outputs = ["calc-x", "calc-y", "calc-yadj"].map((id) => document.getElementById(id));
const format = (value) =>
  `${value.toLocaleString("da-DK", { minimumFractionDigits: 2, maximumFractionDigits: 2 })} cm`;
const clear = () => outputs.forEach((output) => (output.textContent = "—"));

const update = () => {
  const [ttt, ni, hc] = ["ttt", "ni", "hc"].map((name) => Number.parseFloat(form.elements[name].value));
  if ([ttt, ni, hc].some((value) => !Number.isFinite(value) || value <= 0)) {
    clear();
    return;
  }
  const y = 0.2637 * ((ttt + ni) / 2);
  [0.1154 * hc, y, y + 0.35].forEach((value, i) => (outputs[i].textContent = format(value)));
};

form.addEventListener("input", update);
form.addEventListener("submit", (event) => {
  event.preventDefault();
  if (form.reportValidity()) update();
});
form.addEventListener("reset", () => setTimeout(clear, 0));
