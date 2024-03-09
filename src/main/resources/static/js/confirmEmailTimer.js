const timerEl = document.querySelector('.timer-text');
const codeEl = document.querySelector('.confirmation-code');
const btnEl = document.querySelector('.btn');

let timeLeft = 60;

function updateTimer() {
    timeLeft--;
    timerEl.textContent = timeLeft.toString().padStart(2, '0');

    if (timeLeft === 0) {
        btnEl.disabled = true;

        setTimeout(() => {
            window.location.href = document.referrer;
        }, 1000);
    } else {
        setTimeout(updateTimer, 1000);
    }
}

updateTimer();
