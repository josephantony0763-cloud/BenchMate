<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>BenchMate Dashboard</title>

    <link rel="stylesheet" href="assets/css/style.css">
	
	<script src="assets/js/script.js"></script>

    <!-- Lucide Icons -->
    <script src="https://unpkg.com/lucide@latest"></script>
</head>

<body>

<div class="app">

    <!-- ================= SIDEBAR ================= -->

    <aside class="sidebar" id="sidebar">

        <div class="logo">
            <div class="logo-icon">
                <i data-lucide="graduation-cap"></i>
            </div>

            <span>Bench<span>Mate</span></span>
        </div>

        <nav class="sidebar-nav">

            <a href="#" class="nav-item active">
                <i data-lucide="home"></i>
                <span>Home</span>
            </a>

            <a href="#" class="nav-item">
                <i data-lucide="file-text"></i>
                <span>Notes</span>
            </a>

            <a href="#" class="nav-item">
                <i data-lucide="clipboard-list"></i>
                <span>Assignments</span>
            </a>

            <a href="#" class="nav-item">
                <i data-lucide="calendar-days"></i>
                <span>Timetable</span>
            </a>

            <a href="#" class="nav-item">
                <i data-lucide="megaphone"></i>
                <span>Announcements</span>
            </a>

            <a href="#" class="nav-item">
                <i data-lucide="cake"></i>
                <span>Birthdays</span>
            </a>

            <a href="#" class="nav-item">
                <i data-lucide="image"></i>
                <span>Memories</span>
            </a>

            <a href="#" class="nav-item">
                <i data-lucide="users"></i>
                <span>Groups</span>
            </a>

            <a href="#" class="nav-item">
                <i data-lucide="bar-chart-3"></i>
                <span>Attendance</span>
            </a>

            <a href="#" class="nav-item">
                <i data-lucide="badge"></i>
                <span>Marks</span>
            </a>

            <a href="#" class="nav-item">
                <i data-lucide="folder"></i>
                <span>Resources</span>
            </a>

            <a href="#" class="nav-item">
                <i data-lucide="square-check"></i>
                <span>To-Do List</span>
            </a>

            <a href="#" class="nav-item">
                <i data-lucide="settings"></i>
                <span>Settings</span>
            </a>

        </nav>


        <!-- PRO CARD -->

        <div class="pro-card">

            <div class="pro-title">
                 BenchMate Pro
                <span>👑</span>
            </div>

            <p>
                Unlock extra features and
                enhance your experience.
            </p>

            <button>
                Upgrade Now
            </button>

        </div>


        <div class="collapse-sidebar" id="collapseSidebar">

            <i data-lucide="chevron-left"></i>

            <span>Collapse</span>

        </div>

    </aside>


    <!-- ================= MAIN ================= -->

    <main class="main">

        <!-- HEADER -->

        <header class="topbar">

            <button class="menu-button" id="menuButton">
                <i data-lucide="menu"></i>
            </button>


            <div class="search-box">

                <i data-lucide="search"></i>

                <input
                    type="text"
                    placeholder="Search notes, assignments, people..."
                >

            </div>


            <div class="top-actions">

                <button class="icon-button notification-button">

                    <i data-lucide="bell"></i>

                    <span class="notification-badge">
                        3
                    </span>

                </button>


                <button class="icon-button">

                    <i data-lucide="message-circle"></i>

                </button>


                <div class="profile">

                    <div class="profile-avatar">
                        JA
                    </div>

                    <div class="profile-info">

                        <strong>
                            Joseph Antony
                        </strong>

                        <span>
                            CSE, Semester 5
                        </span>

                    </div>

                    <i data-lucide="chevron-down"></i>

                </div>

            </div>

        </header>


        <!-- CONTENT -->

        <div class="content">

            <!-- ================= MAIN COLUMN ================= -->

            <section class="main-column">


                <!-- GREETING -->

                <div class="greeting">

                    <div class="greeting-avatar">
                        JA
                    </div>

                    <div class="greeting-text">

                        <h1>
                            Good Morning, Joseph! 👋
                        </h1>

                        <p>
                            You have
                            <strong>2 assignments</strong>
                            due soon.
                        </p>

                    </div>


                    <div class="date">

                        <strong>
                            Saturday
                        </strong>

                        <span>
                            11 May 2024
                        </span>

                    </div>

                </div>


                <!-- STATISTICS -->

                <div class="stats-grid">


                    <div class="stat-card blue">

                        <div class="stat-icon">
                            <i data-lucide="file-text"></i>
                        </div>

                        <div class="stat-number">
                            2
                        </div>

                        <div class="stat-label">
                            Assignments
                            <br>
                            Due Soon
                        </div>

                        <div class="stat-link">
                            View all
                            <i data-lucide="arrow-right"></i>
                        </div>

                    </div>


                    <div class="stat-card orange">

                        <div class="stat-icon">
                            <i data-lucide="cake"></i>
                        </div>

                        <div class="stat-number">
                            1
                        </div>

                        <div class="stat-label">
                            Birthday
                            <br>
                            Today
                        </div>

                        <div class="stat-link">
                            View all
                            <i data-lucide="arrow-right"></i>
                        </div>

                    </div>


                    <div class="stat-card green">

                        <div class="stat-icon">
                            <i data-lucide="megaphone"></i>
                        </div>

                        <div class="stat-number">
                            3
                        </div>

                        <div class="stat-label">
                            New
                            <br>
                            Announcements
                        </div>

                        <div class="stat-link">
                            View all
                            <i data-lucide="arrow-right"></i>
                        </div>

                    </div>


                    <div class="stat-card purple">

                        <div class="stat-icon">
                            <i data-lucide="folder"></i>
                        </div>

                        <div class="stat-number">
                            5
                        </div>

                        <div class="stat-label">
                            New Notes
                            <br>
                            Uploaded
                        </div>

                        <div class="stat-link">
                            View all
                            <i data-lucide="arrow-right"></i>
                        </div>

                    </div>

                </div>


                <!-- QUICK ACCESS -->

                <div class="section-header">

                    <h2>
                        Quick Access
                    </h2>

                    <button class="customize-button">

                        <i data-lucide="settings"></i>

                        Customize

                    </button>

                </div>


                <div class="quick-access">

                    <div class="quick-item">

                        <div class="quick-icon blue-icon">
                            <i data-lucide="book-open"></i>
                        </div>

                        <span>Notes</span>

                    </div>


                    <div class="quick-item">

                        <div class="quick-icon green-icon">
                            <i data-lucide="clipboard-check"></i>
                        </div>

                        <span>Assignments</span>

                    </div>


                    <div class="quick-item">

                        <div class="quick-icon purple-icon">
                            <i data-lucide="calendar"></i>
                        </div>

                        <span>Timetable</span>

                    </div>


                    <div class="quick-item">

                        <div class="quick-icon orange-icon">
                            <i data-lucide="megaphone"></i>
                        </div>

                        <span>Announcements</span>

                    </div>


                    <div class="quick-item">

                        <div class="quick-icon pink-icon">
                            <i data-lucide="cake"></i>
                        </div>

                        <span>Birthdays</span>

                    </div>


                    <div class="quick-item">

                        <div class="quick-icon cyan-icon">
                            <i data-lucide="image"></i>
                        </div>

                        <span>Memories</span>

                    </div>


                    <div class="quick-item">

                        <div class="quick-icon blue-icon">
                            <i data-lucide="users"></i>
                        </div>

                        <span>Groups</span>

                    </div>


                    <div class="quick-item">

                        <div class="quick-icon yellow-icon">
                            <i data-lucide="bar-chart-3"></i>
                        </div>

                        <span>Attendance</span>

                    </div>

                </div>


                <!-- DEADLINES -->

                <div class="section-header deadline-header">

                    <h2>
                        Upcoming Deadlines
                    </h2>

                    <button>
                        View calendar
                        <i data-lucide="arrow-right"></i>
                    </button>

                </div>


                <div class="deadline-card">


                    <div class="deadline-row">

                        <div class="deadline-icon orange-square">
                            <i data-lucide="file-text"></i>
                        </div>

                        <div class="deadline-info">

                            <strong>
                                Java Assignment
                            </strong>

                            <span>
                                Data Structures
                            </span>

                        </div>

                        <div class="deadline-time urgent">

                            <strong>
                                Due Tomorrow
                            </strong>

                            <span>
                                11 May 2024
                            </span>

                        </div>

                        <span class="priority high">
                            High
                        </span>

                    </div>


                    <div class="deadline-row">

                        <div class="deadline-icon pink-square">
                            <i data-lucide="folder"></i>
                        </div>

                        <div class="deadline-info">

                            <strong>
                                Mini Project Submission
                            </strong>

                            <span>
                                Team Project
                            </span>

                        </div>

                        <div class="deadline-time medium-time">

                            <strong>
                                3 Days Left
                            </strong>

                            <span>
                                14 May 2024
                            </span>

                        </div>

                        <span class="priority medium">
                            Medium
                        </span>

                    </div>


                    <div class="deadline-row">

                        <div class="deadline-icon green-square">
                            <i data-lucide="file-text"></i>
                        </div>

                        <div class="deadline-info">

                            <strong>
                                Internal Exam
                            </strong>

                            <span>
                                Operating Systems
                            </span>

                        </div>

                        <div class="deadline-time low-time">

                            <strong>
                                12 Days Left
                            </strong>

                            <span>
                                23 May 2024
                            </span>

                        </div>

                        <span class="priority low">
                            Low
                        </span>

                    </div>


                    <div class="view-all-bottom">

                        <button>
                            View all deadlines
                            <i data-lucide="arrow-right"></i>
                        </button>

                    </div>

                </div>


            </section>


            <!-- ================= RIGHT COLUMN ================= -->

            <aside class="right-column">


                <!-- BIRTHDAY -->

                <div class="birthday-card">

                    <div class="birthday-header">

                        <h2>
                            🎉 Birthday Today
                        </h2>

                        <button>
                            See all
                        </button>

                    </div>


                    <div class="birthday-content">

                        <div class="birthday-avatar">
                            RS
                        </div>

                        <div class="birthday-info">

                            <strong>
                                Rahul Sharma
                            </strong>

                            <p>
                                Wishing you a
                                <br>
                                wonderful year ahead!
                            </p>

                            <button class="wish-button">
                                Wish Now
                            </button>

                        </div>

                        <div class="party-icon">
                            🎉
                        </div>

                    </div>

                </div>


                <!-- DOTS -->

                <div class="carousel-dots">

                    <span class="dot active"></span>
                    <span class="dot"></span>
                    <span class="dot"></span>

                </div>


                <!-- ANNOUNCEMENTS -->

                <div class="panel">

                    <div class="panel-header">

                        <h2>
                            Latest Announcements
                        </h2>

                        <button>
                            View all
                            <i data-lucide="arrow-right"></i>
                        </button>

                    </div>


                    <div class="announcement-list">


                        <div class="announcement">

                            <div class="announcement-icon blue-square">
                                <i data-lucide="megaphone"></i>
                            </div>

                            <div>

                                <strong>
                                    Tomorrow Holiday
                                </strong>

                                <span>
                                    10 minutes ago
                                </span>

                            </div>

                            <span class="new-badge">
                                New
                            </span>

                        </div>


                        <div class="announcement">

                            <div class="announcement-icon green-square">
                                <i data-lucide="file-text"></i>
                            </div>

                            <div>

                                <strong>
                                    Java Assignment Extended
                                </strong>

                                <span>
                                    Yesterday
                                </span>

                            </div>

                        </div>


                        <div class="announcement">

                            <div class="announcement-icon purple-square">
                                <i data-lucide="calendar"></i>
                            </div>

                            <div>

                                <strong>
                                    Internal Exam Schedule
                                </strong>

                                <span>
                                    2 days ago
                                </span>

                            </div>

                        </div>

                    </div>


                    <div class="panel-bottom">

                        <button>
                            View all announcements
                            <i data-lucide="arrow-right"></i>
                        </button>

                    </div>

                </div>


                <!-- TODO -->

                <div class="panel todo-panel">

                    <div class="panel-header">

                        <h2>
                            My To-Do
                        </h2>

                        <button>
                            View all
                            <i data-lucide="arrow-right"></i>
                        </button>

                    </div>


                    <div class="todo-list">


                        <div class="todo-item">

                            <input type="checkbox">

                            <div class="todo-content">

                                <strong>
                                    Finish OS Notes
                                </strong>

                                <span class="todo-danger">
                                    Today, 6:00 PM
                                </span>

                            </div>

                            <span class="priority high">
                                High
                            </span>

                        </div>


                        <div class="todo-item">

                            <input type="checkbox">

                            <div class="todo-content">

                                <strong>
                                    Prepare DBMS Assignment
                                </strong>

                                <span>
                                    Tomorrow, 10:00 AM
                                </span>

                            </div>

                            <span class="priority medium">
                                Medium
                            </span>

                        </div>


                        <div class="todo-item">

                            <input type="checkbox">

                            <div class="todo-content">

                                <strong>
                                    Revise Data Structures
                                </strong>

                                <span>
                                    14 May 2024
                                </span>

                            </div>

                            <span class="priority low">
                                Low
                            </span>

                        </div>

                    </div>


                    <div class="panel-bottom">

                        <button>
                            View all tasks
                            <i data-lucide="arrow-right"></i>
                        </button>

                    </div>

                </div>


            </aside>

        </div>


        <!-- FOOTER -->

        <footer class="footer">

            <span>
                © 2024 BenchMate. All rights reserved.
            </span>

            <div class="footer-links">

                <a href="#">
                    Privacy Policy
                </a>

                <a href="#">
                    Terms of Service
                </a>

                <a href="#">
                    Contact Us
                </a>

                <button id="darkModeButton">

                    <i data-lucide="moon"></i>

                    Dark Mode

                    <span class="toggle"></span>

                </button>

            </div>

        </footer>

    </main>

</div>


</body>
</html>