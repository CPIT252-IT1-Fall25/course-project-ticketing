// User Greeting functionality
(function() {
    'use strict';
    
    function displayUserGreeting() {
        // Fetch user info from server
        fetch('/userinfo')
            .then(response => response.json())
            .then(data => {
                if (data.loggedIn && data.userName) {
                    // Find the greeting element or create it
                    let greetingElement = document.getElementById('user-greeting');
                    
                    if (!greetingElement) {
                        // Find the header links container (try both header types)
                        let headerLinks = document.querySelector('.header .header-Links');
                        if (!headerLinks) {
                            headerLinks = document.querySelector('.header-logIn .header-Links');
                        }
                        
                        if (headerLinks) {
                            // Create greeting element
                            greetingElement = document.createElement('span');
                            greetingElement.id = 'user-greeting';
                            greetingElement.className = 'user-greeting';
                            greetingElement.textContent = `Welcome, ${data.userName}`;
                            
                            // Insert before the switch or at the beginning
                            const switchElement = headerLinks.querySelector('.switch');
                            if (switchElement) {
                                headerLinks.insertBefore(greetingElement, switchElement);
                            } else {
                                headerLinks.insertBefore(greetingElement, headerLinks.firstChild);
                            }
                        }
                    } else {
                        // Update existing greeting
                        greetingElement.textContent = `Welcome, ${data.userName}`;
                        greetingElement.style.display = 'inline-block';
                    }
                    
                    // Hide or modify the "Create Account" button if it exists
                    const createAccountBtn = document.querySelector('a[href="CreatAccount.html"]');
                    if (createAccountBtn && createAccountBtn.textContent.includes('Create Account')) {
                        createAccountBtn.style.display = 'none';
                    }
                    
                    // Hide "Sign in" button if it exists
                    const signInBtn = document.querySelector('button.header-Links');
                    if (signInBtn && (signInBtn.textContent.includes('Sign in') || signInBtn.textContent.includes('Sign In'))) {
                        signInBtn.style.display = 'none';
                    }
                } else {
                    // User not logged in - hide greeting if it exists
                    const greetingElement = document.getElementById('user-greeting');
                    if (greetingElement) {
                        greetingElement.style.display = 'none';
                    }
                }
            })
            .catch(error => {
                console.error('Error fetching user info:', error);
            });
    }
    
    // Initialize when DOM is ready
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', displayUserGreeting);
    } else {
        displayUserGreeting();
    }
})();

