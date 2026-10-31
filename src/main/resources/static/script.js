// =========================
// YES BUTTON
// =========================

function sayYes() {

    window.location.href = "date.html";
}


// =========================
// NO BUTTON
// =========================

function sayNo() {

    const noButton = document.querySelector(".no-btn");

    if (!noButton) {
        return;
    }

    noButton.addEventListener("mouseover", function () {

        const maxX =
            window.innerWidth - noButton.offsetWidth;

        const maxY =
            window.innerHeight - noButton.offsetHeight;

        const randomX =
            Math.random() * maxX;

        const randomY =
            Math.random() * maxY;

        noButton.style.position = "fixed";

        noButton.style.left =
            randomX + "px";

        noButton.style.top =
            randomY + "px";
    });
}


// =========================
// DATE FORM
// =========================

document
    .getElementById("dateForm")
    ?.addEventListener("submit", function (event) {

        event.preventDefault();

        const name =
            document.getElementById("name").value;

        const mobile =
            document.getElementById("mobile").value;

        const date =
            document.getElementById("date").value;

        const time =
            document.getElementById("time").value;

        const message =
            document.getElementById("message").value;


        // =========================
        // REQUEST DATA
        // =========================

        const requestData = {

            name: name,

            answer: "YES",

            mobile: mobile,

            date: formatDate(date),

            time: time,

            message: message
        };


        console.log("Sending data:", requestData);


        // =========================
        // SEND TO SPRING BOOT
        // =========================

        fetch("/save/schedule", {

            method: "POST",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify(requestData)

        })

        .then(async response => {

            const data =
                await response.json().catch(() => ({}));


            if (response.status === 401) {

                window.location.href = "login.html";

                return null;
            }


            if (!response.ok) {

                throw new Error(
                    data.error ||
                    "Failed to save date request"
                );
            }


            return data;

        })

        .then(data => {

            if (!data) {
                return;
            }


            document.getElementById("result").innerText =
                data.status ||
                "Date successfully saved ❤️";


            console.log(
                "Backend response:",
                data
            );

        })

        .catch(error => {

            document.getElementById("result").innerText =
                error.message ||
                "Something went wrong ❌";


            console.error(error);

        });

    });


// =========================
// FORMAT DATE
// =========================

function formatDate(dateValue) {

    const parts =
        dateValue.split("-");


    return (
        parts[2] +
        "-" +
        parts[1] +
        "-" +
        parts[0]
    );
}