document.addEventListener("DOMContentLoaded", function () {

    /* ================= LUCIDE ICONS ================= */

    lucide.createIcons();


    /* ================= SIDEBAR ================= */

    const menuButton =
        document.getElementById("menuButton");

    const sidebar =
        document.getElementById("sidebar");


    if (menuButton && sidebar) {

        menuButton.addEventListener("click", function () {

            sidebar.classList.toggle("open");

        });

    }


    /* ================= SIDEBAR NAVIGATION ================= */

    const navItems =
        document.querySelectorAll(".nav-item");


    navItems.forEach(function (item) {

        item.addEventListener("click", function (event) {

            event.preventDefault();

            navItems.forEach(function (nav) {

                nav.classList.remove("active");

            });

            item.classList.add("active");


            /* Close sidebar on mobile */

            if (
                window.innerWidth <= 900 &&
                sidebar
            ) {

                sidebar.classList.remove("open");

            }

        });

    });


    /* ================= COLLAPSE SIDEBAR ================= */

    const collapseSidebar =
        document.getElementById("collapseSidebar");


    if (collapseSidebar) {

        collapseSidebar.addEventListener("click", function () {

            if (window.innerWidth > 900) {

                sidebar.classList.toggle("collapsed");

            }

        });

    }


    /* ================= TODO ================= */

    const todoCheckboxes =
        document.querySelectorAll(
            '.todo-item input[type="checkbox"]'
        );


    todoCheckboxes.forEach(function (checkbox) {

        checkbox.addEventListener("change", function () {

            const todoItem =
                checkbox.closest(".todo-item");

            const todoText =
                todoItem.querySelector("strong");


            if (checkbox.checked) {

                todoText.style.textDecoration =
                    "line-through";

                todoText.style.opacity =
                    "0.5";

                todoItem.style.opacity =
                    "0.65";

            } else {

                todoText.style.textDecoration =
                    "none";

                todoText.style.opacity =
                    "1";

                todoItem.style.opacity =
                    "1";

            }

        });

    });


    /* ================= DARK MODE ================= */

    const darkModeButton =
        document.getElementById("darkModeButton");


    if (darkModeButton) {

        darkModeButton.addEventListener(
            "click",
            function () {

                document.body.classList.toggle("dark");


                const darkModeEnabled =
                    document.body.classList.contains("dark");


                localStorage.setItem(
                    "benchmate-dark-mode",
                     darkModeEnabled
                );

            }
        );

    }


    /* ================= LOAD DARK MODE ================= */

    const savedDarkMode =
        localStorage.getItem(
        "benchmate-dark-mode"
    );


    if (savedDarkMode === "true") {

        document.body.classList.add("dark");

    }


    /* ================= WISH BUTTON ================= */

    const wishButton =
        document.querySelector(".wish-button");


    if (wishButton) {

        wishButton.addEventListener(
            "click",
            function () {

                wishButton.textContent =
                    "Wished ✓";

                wishButton.style.background =
                    "#16a34a";

            }
        );

    }


    /* ================= STAT CARDS ================= */

    const statCards =
        document.querySelectorAll(".stat-card");


    statCards.forEach(function (card) {

        card.addEventListener("click", function () {

            card.style.transform =
                "scale(0.98)";


            setTimeout(function () {

                card.style.transform =
                    "";

            }, 120);

        });

    });


    /* ================= QUICK ACCESS ================= */

    const quickItems =
        document.querySelectorAll(".quick-item");


    quickItems.forEach(function (item) {

        item.addEventListener("click", function () {

            const page =
                item.querySelector("span").textContent;

            console.log(
                "Opening:",
                page
            );

        });

    });


    /* ================= SEARCH ================= */

    const searchInput =
        document.querySelector(
            ".search-box input"
        );


    if (searchInput) {

        searchInput.addEventListener(
            "input",
            function () {

                const query =
                    searchInput.value
                        .toLowerCase()
                        .trim();


                const searchableItems =
                    document.querySelectorAll(
                        ".deadline-row, .announcement, .todo-item"
                    );


                searchableItems.forEach(
                    function (item) {

                        const text =
                            item.textContent
                                .toLowerCase();


                        if (
                            query === "" ||
                            text.includes(query)
                        ) {

                            item.style.display =
                                "";

                        } else {

                            item.style.display =
                                "none";

                        }

                    }
                );

            }
        );

    }


    /* ================= NOTIFICATION ================= */

    const notificationButton =
        document.querySelector(
            ".notification-button"
        );


    if (notificationButton) {

        notificationButton.addEventListener(
            "click",
            function () {

                const badge =
                    document.querySelector(
                        ".notification-badge"
                    );


                if (badge) {

                    badge.style.display =
                        "none";

                }

            }
        );

    }

});