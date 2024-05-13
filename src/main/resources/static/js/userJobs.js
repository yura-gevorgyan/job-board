$(document).ready(function () {
    var currentPage = 1; // Initial page number
    var userId = null;
    var searchQuery = '';
    var totalPages = 0;// Initialize userId

    // Function to create job divs
    function createJobDiv(job, resumeId) {

        var jobDiv = document.createElement('div');
        jobDiv.classList.add('col-md-6', 'mt-1-9');

        var cardDiv = document.createElement('div');
        cardDiv.classList.add('card', 'card-style10');

        var cardBodyDiv = document.createElement('div');
        cardBodyDiv.classList.add('card-body');

        // Populate card body with job details
        cardBodyDiv.innerHTML = `
        <span class="popular-jobs-status">${job.status.replaceAll('_', ' ').toLowerCase()}</span>
        <div class="popular-jobs-box">
            <img class="mb-4 border-radius-10" src="/download/picture/${job.logoName}" width="60px" alt="...">
            <h4 class="h5">${job.title}</h4>
            <p class="text-muted font-weight-500">${job.category.name}</p>
        </div>
        <span class="border-end border-color-extra-light-gray pe-2 me-2"><i class="fas fa-map-marker-alt pe-2 text-secondary"></i><span>${job.country.countryName}</span></span>
        <span class="border-end border-color-extra-light-gray pe-2 me-2"><i class="far fa-clock pe-2 text-secondary"></i><span>${job.publishedDate.substring(0, 10)}</span></span>
        <br>
        <span><i class="ti-briefcase pe-2 text-secondary"></i>${job.workExperience.replaceAll('_', ' ').toUpperCase()}</span>
       
        <a href="/resumes/apply/${resumeId}?jobId=${job.id}" class="butn butn-apply">Choose this Job</a>
    `;

        // Append elements
        cardDiv.appendChild(cardBodyDiv);
        jobDiv.appendChild(cardDiv);

        return jobDiv;
    }

    // Function to load paginated content via AJAX
    function loadContent(page, userId, resumeId, searchQuery) {

        var url = '/user-jobs?page=' + page + '&userId=' + userId;
        if (searchQuery !== '') {
            url += '&search=' + encodeURIComponent(searchQuery);
        }
        $.ajax({
            url: url,
            method: 'GET',
        success: function (response) {
                console.log(response)
                if (!response.content.length && searchQuery === ''){
                    let element = document.querySelector(".modal-content-pop");
                    element.innerHTML = ``;

                    element.innerHTML = `
                     <span>
                            <div style="display: flex; justify-content: center" class="alert alert-warning alert-dismissible ">
                                <strong>Please create Job for apply this resume !!!</strong>
                            </div>
                    </span>
                    
                    <a href="/profile/jobs-create" class="butn mb-3 w-100 text-center">Create Job</a>
                    `;
                }
            // Clear existing content
            $('.user-jobs-popup').empty();
            // Append new job divs
            response.content.forEach(function (job) {
                var jobDiv = createJobDiv(job, resumeId);
                $('.user-jobs-popup').append(jobDiv);
            });
            // Update pagination info if needed
            $('.currentPage').text(response.currentPage);
            $('.totalPages').text(response.totalPages);
            var currentPageElement = document.querySelector('.currentPage');
            currentPageElement.textContent = currentPage;
            totalPages = response.totalPages;
        }
    ,
        error: function (xhr, status, error) {
            // Handle errors if any
            console.error(error);
        }
    })
        ;
    }

    // Click event handler for elements with class .user-jobs
    $('.user-jobs').on('click', function (e) {
        currentPage = 1; // Reset currentPage when user clicks on 'Apply For Job'
        userId = $(this).data('user-id');
        resumeId = $(this).data('resume-id');
        loadContent(currentPage, userId, resumeId,searchQuery);
    });

    $('.search').on('input', function () {
        searchQuery = $(this).val();
        currentPage = 1;
        loadContent(currentPage, userId, resumeId, searchQuery);
    });

    // Click event handler for pagination buttons
    $('.prevBtn').on('click', function () {
        if (currentPage > 1) {
            currentPage--;
            loadContent(currentPage, userId,resumeId,searchQuery);
        }
    });

    $('.nextBtn').on('click', function () {
        // You might need to handle the case when the currentPage equals totalPages
        if (currentPage + 1  <= totalPages){
            currentPage++;
            loadContent(currentPage, userId,resumeId,searchQuery);
        }
    });

});