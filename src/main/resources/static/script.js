// Function to create the new student
function createStudent() {
    const studentName = document.getElementById('sName').value.trim();
    const department = document.getElementById('department').value.trim();
    const courseName = document.getElementById('courseName').value.trim();

    if (!studentName || !department || !courseName) {
        alert("Please fill in all fields.");
        return;
    }

    const student = {
        studentName: studentName,
        department: department,
        courseName: courseName
    };

    const url = 'https://bonafidegenerator.up.railway.app/students/create';

    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(student)
    })
        .then(response => response.json())
        .then(data => {
            const msgBox = document.getElementById('createStudentMessage');
            msgBox.style.display = 'block';
            msgBox.innerHTML = data.message === 'Student created successfully' ? data.message : "Error: " + data.message;
        })
        .catch(error => {
            console.error('Error creating student:', error);
            const msgBox = document.getElementById('createStudentMessage');
            msgBox.style.display = 'block';
            msgBox.innerHTML = "Error creating student.";
        });
}

document.addEventListener("DOMContentLoaded", function () {
    const createStudentBtn = document.getElementById('createStudentBtn');
    if (createStudentBtn) {
        createStudentBtn.addEventListener('click', createStudent);
    }
});

// Function to fetch and display all students
function getAllStudents() {
    const url = 'https://bonafidegenerator.up.railway.app/students/all';

    fetch(url)
        .then(response => response.json())
        .then(students => {
            const studentsTableBody = document.querySelector('#studentsTable tbody');
            studentsTableBody.innerHTML = '';

            students.forEach(student => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td><a href="#" class="downloadPdf" data-id="${student.studentId}">${student.studentId}</a></td>
                    <td>${student.studentName}</td>
                    <td>${student.department}</td>
                    <td>${student.courseName}</td>
                    <td>${student.issueDate}</td>
                `;
                studentsTableBody.appendChild(row);
            });

            // Attach event listener for downloading PDF on student ID click with confirmation
            document.querySelectorAll('.downloadPdf').forEach(item => {
                item.addEventListener('click', function (event) {
                    event.preventDefault();
                    const studentId = this.dataset.id;
                    const confirmDownload = confirm(`Do you want to download the certificate for Student ID: ${studentId}?`);
                    if (confirmDownload) {
                        createPdf(studentId);
                    }
                });
            });
        })
        .catch(error => {
            console.error('Error fetching students:', error);
        });
}

document.addEventListener("DOMContentLoaded", function () {
    getAllStudents();
});

// Function to generate PDF based on the student ID (used by student ID link)
function createPdf(studentId) {
    document.getElementById('loadingMessage').style.display = 'block';
    document.getElementById('errorMessage').style.display = 'none';

    const url = `https://bonafidegenerator.up.railway.app/students/pdf/${studentId}`;

    fetch(url, { method: 'GET' })
        .then(response => {
            if (!response.ok) throw new Error('Student not found!');
            return response.blob();
        })
        .then(pdfBlob => {
            document.getElementById('loadingMessage').style.display = 'none';

            const downloadLink = document.createElement('a');
            const blobUrl = window.URL.createObjectURL(pdfBlob);
            downloadLink.href = blobUrl;
            downloadLink.download = `${studentId}_bonafide_certificate.pdf`;
            downloadLink.click();
        })
        .catch(error => {
            document.getElementById('loadingMessage').style.display = 'none';
            document.getElementById('errorMessage').style.display = 'block';
            document.getElementById('errorMessage').innerHTML = error.message;
        });
}

// Function for "Generate Certificate" button
function generatePdf() {
    const studentId = document.getElementById('studentId').value.trim();

    if (!studentId) {
        alert("Please enter a student ID.");
        return;
    }

    const confirmDownload = confirm(`Do you want to download the certificate for Student ID: ${studentId}?`);
    if (!confirmDownload) {
        return;
    }

    document.getElementById('loadingMessage').style.display = 'block';
    document.getElementById('errorMessage').style.display = 'none';

    const url = `https://bonafidegenerator.up.railway.app/students/pdf/${studentId}`;

    fetch(url, { method: 'GET' })
        .then(response => {
            if (!response.ok) {
                throw new Error('Student not found!');
            }
            return response.blob();
        })
        .then(pdfBlob => {
            document.getElementById('loadingMessage').style.display = 'none';

            const downloadLink = document.createElement('a');
            const url = window.URL.createObjectURL(pdfBlob);
            downloadLink.href = url;
            downloadLink.download = `${studentId}_bonafide_certificate.pdf`;
            downloadLink.click();
        })
        .catch(error => {
            document.getElementById('loadingMessage').style.display = 'none';
            document.getElementById('errorMessage').style.display = 'block';
            document.getElementById('errorMessage').innerHTML = error.message;
        });
}

// Wait for the DOM to fully load before attaching event listener to "Generate Certificate" button
document.addEventListener("DOMContentLoaded", function () {
    const generateBtn = document.getElementById('generateBtn');
    if (generateBtn) {
        generateBtn.addEventListener('click', generatePdf);
    }
});
