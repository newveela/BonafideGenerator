// Function to create the new student
function createStudent() {
    const studentName = document.getElementById('sName').value.trim();
    const department = document.getElementById('department').value.trim();
    const courseName = document.getElementById('courseName').value.trim();

    // Check if all fields are filled
    if (!studentName || !department || !courseName) {
        alert("Please fill in all fields.");
        return;
    }

    const student = {
        studentName: studentName,
        department: department,
        courseName: courseName
    };

    const url = 'http://localhost:8080/students/create';

    // Send POST request to create the new student
    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(student)
    })
        .then(response => {
            return response.json(); // Parse the JSON response
        })
        .then(data => {
            // Check the response and display the appropriate message
            if (data.message === 'Student created successfully') {
                document.getElementById('createStudentMessage').style.display = 'block';
                document.getElementById('createStudentMessage').innerHTML = data.message;
            } else {
                document.getElementById('createStudentMessage').style.display = 'block';
                document.getElementById('createStudentMessage').innerHTML = "Error: " + data.message;
            }
        })
        .catch(error => {
            console.error('Error creating student:', error);
            document.getElementById('createStudentMessage').style.display = 'block';
            document.getElementById('createStudentMessage').innerHTML = "Error creating student.";
        });
}

// Wait for the DOM to fully load before attaching event listeners
document.addEventListener("DOMContentLoaded", function() {
    const createStudentBtn = document.getElementById('createStudentBtn');
    if (createStudentBtn) {
        createStudentBtn.addEventListener('click', createStudent);
    }
});
// Function to fetch and display all students
function getAllStudents() {
    const url = 'http://localhost:8080/students/all';  // Ensure the URL is correct for your backend

    // Send GET request to fetch all students
    fetch(url)
        .then(response => response.json()) // Parse the JSON response
        .then(students => {
            // Get the table body element
            const studentsTableBody = document.querySelector('#studentsTable tbody');
            studentsTableBody.innerHTML = ''; // Clear the existing table data (if any)

            // Loop through the students and append rows to the table
            students.forEach(student => {
                const row = document.createElement('tr');

                // Create each table cell and append to the row
                row.innerHTML = `
          <td>${student.studentId}</td>
          <td>${student.studentName}</td>
          <td>${student.department}</td>
          <td>${student.courseName}</td>
          <td>${student.issueDate}</td>
        `;

                // Append the row to the table body
                studentsTableBody.appendChild(row);
            });
        })
        .catch(error => {
            console.error('Error fetching students:', error);
        });
}

// Wait for the DOM to fully load before calling the function to display all students
document.addEventListener("DOMContentLoaded", function() {
    // Fetch all students when the page is loaded
    getAllStudents();
});


// Function to generate PDF based on the student ID
function generatePdf() {
    const studentId = document.getElementById('studentId').value.trim();

    // Check if the student ID field is not empty
    if (!studentId) {
        alert("Please enter a student ID.");
        return;
    }

    // Show loading message while the PDF is being generated
    document.getElementById('loadingMessage').style.display = 'block';
    document.getElementById('errorMessage').style.display = 'none';

    // Prepare the URL to fetch the student details and generate the PDF
    const url = `http://localhost:8080/api/pdf/generate/${studentId}`;  // Make sure this URL is correct

    // Fetch the student details and generate the PDF
    fetch(url, { method: 'GET' })
        .then(response => {
            if (!response.ok) {
                throw new Error('Student not found!');
            }
            return response.blob(); // Get the response as a blob (PDF file)
        })
        .then(pdfBlob => {
            // Hide the loading message
            document.getElementById('loadingMessage').style.display = 'none';

            // Create a download link for the generated PDF
            const downloadLink = document.createElement('a');
            const url = window.URL.createObjectURL(pdfBlob);
            downloadLink.href = url;
            downloadLink.download = `${studentId}_bonafide_certificate.pdf`; // Set filename
            downloadLink.click();  // Trigger the download
        })
        .catch(error => {
            // Hide the loading message and show the error message
            document.getElementById('loadingMessage').style.display = 'none';
            document.getElementById('errorMessage').style.display = 'block';
            document.getElementById('errorMessage').innerHTML = error.message;  // Display error message
        });
}

// Wait for the DOM to fully load before attaching event listeners
document.addEventListener("DOMContentLoaded", function() {
    // Attach the event listener to the "Generate Certificate" button
    const generateBtn = document.getElementById('generateBtn');
    if (generateBtn) {
        generateBtn.addEventListener('click', generatePdf);
    }
});
