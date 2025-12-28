// Theme Switcher functionality
(function() {
    'use strict';
    
    // Get or set theme from localStorage
    function getTheme() {
        return localStorage.getItem('theme') || 'dark';
    }
    
    function setTheme(theme) {
        localStorage.setItem('theme', theme);
        applyTheme(theme);
    }
    
    // Apply theme to the page
    function applyTheme(theme) {
        const html = document.documentElement;
        const body = document.body;
        
        if (theme === 'light') {
            html.classList.add('light-theme');
            html.classList.remove('dark-theme');
            if (body) {
                body.classList.add('light-theme');
                body.classList.remove('dark-theme');
            }
        } else {
            html.classList.remove('light-theme');
            html.classList.add('dark-theme');
            if (body) {
                body.classList.remove('light-theme');
                body.classList.add('dark-theme');
            }
        }
    }
    
    // Initialize theme on page load
    function initTheme() {
        const currentTheme = getTheme();
        applyTheme(currentTheme);
        
        // Set switch state
        const themeSwitch = document.querySelector('.switch input[type="checkbox"]');
        if (themeSwitch) {
            themeSwitch.checked = (currentTheme === 'light');
        }
    }
    
    // Handle switch toggle
    function setupThemeSwitch() {
        const themeSwitch = document.querySelector('.switch input[type="checkbox"]');
        if (themeSwitch) {
            themeSwitch.addEventListener('change', function() {
                const newTheme = this.checked ? 'light' : 'dark';
                setTheme(newTheme);
            });
        }
    }
    
    // Initialize when DOM is ready
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', function() {
            initTheme();
            setupThemeSwitch();
        });
    } else {
        initTheme();
        setupThemeSwitch();
    }
})();

