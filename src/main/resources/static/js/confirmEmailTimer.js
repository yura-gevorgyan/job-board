const timerEl = document.querySelector('.timer-text');
const codeEl = document.querySelector('.confirmation-code');
const btnEl = document.querySelector('.btn');

let timeLeft = 60;

setInterval(() => {
    timeLeft--;
    timerEl.textContent = timeLeft.toString().padStart(2, '0');

    if (timeLeft === 0) {
        btnEl.disabled = true;
    }
}, 1000);
