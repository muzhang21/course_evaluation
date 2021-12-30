/**
 * modified by Mu
 */
document.addEventListener("DOMContentLoaded", init);

function init() {

	// get all the elements with a class name of clink
	links = document.getElementsByClassName("clink");

    // add a click event handler to each link
	for (let item of links) {
		item.addEventListener("click", function() {

			div = document.getElementById("display");  // display div

			// invoke the handler that gets a course by code
			fetch('https://localhost:8443/evaluation/get/' + item.code)
				.then(data => data.json()) // convert to json
				
				// function to execute on the json data
				.then(function(data) {
					var output = "Course Code: " + data.course.code + "<br>"
						+ "Title: " + data.course.title + "<br>"
						+ "Credits: " + data.course.credits;

					// finally, display the container output in the div
					div.innerHTML = output;
				});

		});
	}

}