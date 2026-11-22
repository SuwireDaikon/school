SELECT s.name AS student_name, s.age, f.name AS faculty_name
FROM student s
LEFT JOIN faculty f ON f.id = s.faculty_id;

SELECT s.name AS student_name, s.age
FROM student s
INNER JOIN avatars a ON a.student_id = s.id;