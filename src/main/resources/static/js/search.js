document.addEventListener("DOMContentLoaded", function () {
    // Get all job advertisements
    const jobAdvertisements = document.querySelectorAll(".job-advertisement");

    // Function to perform search
    function performSearch() {
        const searchInput = document.getElementById("search").value.toLowerCase();

        // Iterate through each job advertisement
        jobAdvertisements.forEach(function (advertisement) {
            const title = advertisement.querySelector(".card-body h4").textContent.toLowerCase();

            // Check if any of the advertisement details contain the search query
            if (title.includes(searchInput)) {
                advertisement.classList.remove("d-none"); // Show the advertisement
            } else {
                advertisement.classList.add("d-none"); // Hide the advertisement
            }
        });
    }

    // Add event listener to search button
    document.getElementById("search").addEventListener("input", performSearch);
});