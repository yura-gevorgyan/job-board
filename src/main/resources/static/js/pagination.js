document.addEventListener("DOMContentLoaded", function () {
    var prevBtn = document.getElementById("prevBtn");
    var nextBtn = document.getElementById("nextBtn");
    var currentPageSpan = document.getElementById("currentPage");
    var totalPagesSpan = document.getElementById("totalPages");
    var pageSize = 4; // Adjust this value as per your requirement

    var currentPage = parseInt(currentPageSpan.textContent);
    var totalPages = parseInt(totalPagesSpan.textContent);


    // Event listener for previous button
    prevBtn.addEventListener("click", function () {
        if (currentPage > 1) {
            currentPage--;
            updatePage();
        }
    });

    // Event listener for next button
    nextBtn.addEventListener("click", function () {
        if (currentPage < totalPages) {
            currentPage++;
            updatePage();
        }
    });

    // Function to update page content
    function updatePage() {
        currentPageSpan.textContent = currentPage;

        var jobAds = document.querySelectorAll(".job-advertisement");
        var startIndex = (currentPage - 1) * pageSize;
        var endIndex = Math.min(startIndex + pageSize, jobAds.length);

        // Hide all job advertisements
        jobAds.forEach(function (ad) {
            ad.style.display = "none";
        });

        // Display job advertisements for the current page
        for (var i = startIndex; i < endIndex; i++) {
            jobAds[i].style.display = "block";
            console.log(jobAds[i]);
        }
    }
});