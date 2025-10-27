"use client"
import React, { useState, useEffect } from 'react';
import Link from 'next/link';
import { useRouter } from 'next/navigation';
import Cookies from 'js-cookie';

const Header = () => {
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const router = useRouter();

  // Check for the token when the component mounts
  useEffect(() => {
    const token = Cookies.get('jwt_token');
    if (token) {
      setIsLoggedIn(true);
    }
  }, []);

  const handleLogout = () => {
    // Remove the cookie
    Cookies.remove('jwt_token');
    // Update the state
    setIsLoggedIn(false);
    // Redirect to the login page
    router.push('/Login');
  };

  return (
    <header className="bg-blue-800 text-white shadow-md">
      <div className="container mx-auto px-4 py-3 flex justify-between items-center">
        {/* Logo */}
        <Link href="/" className="text-2xl font-bold">MyBank</Link>

        {/* Desktop Navigation */}
        <nav className="hidden md:flex items-center space-x-6">
          {isLoggedIn ? (
            <>
              {/* --- LOGGED-IN NAVIGATION --- */}
              <Link href="/dashboard" className="hover:text-blue-200 transition-colors">Dashboard</Link>
              <Link href="/profile" className="hover:text-blue-200 transition-colors">Profile</Link>
              <Link href="/transfer" className="hover:text-blue-200 transition-colors">Transfer</Link>
              <Link href="/transactions" className="hover:text-blue-200 transition-colors">Transactions</Link>
            </>
          ) : (
            <>
              {/* --- LOGGED-OUT NAVIGATION --- */}
              <Link href="/about" className="hover:text-blue-200 transition-colors">About Us</Link>
              <Link href="/services" className="hover:text-blue-200 transition-colors">Services</Link>
              <Link href="/contact" className="hover:text-blue-200 transition-colors">Contact</Link>
            </>
          )}
        </nav>

        {/* CTA Buttons & Logout */}
        <div className="hidden md:flex items-center space-x-4">
          {isLoggedIn ? (
            // --- LOGGED-IN BUTTON ---
            <button
              onClick={handleLogout}
              className="bg-red-600 text-white py-2 px-4 rounded-md font-semibold hover:bg-red-700 transition-colors"
            >
              Logout
            </button>
          ) : (
            // --- LOGGED-OUT BUTTONS ---
            <>
              <Link href="/Login" className="bg-gray-100 text-blue-800 py-2 px-4 rounded-md font-semibold hover:bg-gray-200 transition-colors">
                Login
              </Link>
              <Link href="/Register" className="bg-blue-600 text-white py-2 px-4 rounded-md font-semibold hover:bg-blue-700 transition-colors">
                Open Account
              </Link>
            </>
          )}
        </div>

        {/* Mobile Menu Button (functionality not fully implemented for brevity) */}
        <div className="md:hidden">
          <button onClick={() => setIsMenuOpen(!isMenuOpen)}>
            {/* SVG Icon */}
          </button>
        </div>
      </div>
      
      {/* Note: The mobile menu would also need to be made conditional like the desktop view */}
    </header>
  );
};

export default Header;