"use client"
import React, { useState } from 'react';
import Link from 'next/link';

const Header = () => {
  const [isMenuOpen, setIsMenuOpen] = useState(false);

  return (
    <header className="bg-blue-800 text-white shadow-md">
      <div className="container mx-auto px-4 py-3 flex justify-between items-center">
        {/* Logo */}
        <Link href="/" className="text-2xl font-bold">
          MyBank
        </Link>

        {/* Desktop Navigation */}
        <nav className="hidden md:flex items-center space-x-6">

          <Link href="/contact" className="hover:text-blue-200 transition-colors">Home</Link>
          <Link href="/contact" className="hover:text-blue-200 transition-colors">Profile</Link>
          <Link href="/contact" className="hover:text-blue-200 transition-colors">Transfer</Link>
          <Link href="/contact" className="hover:text-blue-200 transition-colors">Transaction</Link>
          <Link href="/contact" className="hover:text-blue-200 transition-colors">Auditor Dashboard</Link>
          {/* <Link href="/contact" className="hover:text-blue-200 transition-colors">Logout</Link> */}

        </nav>

        {/* CTA Buttons */}
        <div className="hidden md:flex items-center space-x-4">
          <Link href="/Login" className="bg-gray-100 text-blue-800 py-2 px-4 rounded-md font-semibold hover:bg-gray-200 transition-colors">
            Login
          </Link>
          <Link href="/Register" className="bg-blue-600 text-white py-2 px-4 rounded-md font-semibold hover:bg-blue-700 transition-colors">
            Open Account
          </Link>
        </div>

        {/* Mobile Menu Button */}
        <div className="md:hidden">
          <button onClick={() => setIsMenuOpen(!isMenuOpen)}>
            <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M4 6h16M4 12h16m-7 6h7"></path>
            </svg>
          </button>
        </div>
      </div>

      {/* Mobile Menu */}
      {isMenuOpen && (
        <div className="md:hidden bg-blue-700">
          <nav className="flex flex-col items-center space-y-4 py-4">
            <Link href="/personal" className="hover:text-blue-200 transition-colors">Personal</Link>
            <Link href="/business" className="hover:text-blue-200 transition-colors">Business</Link>
            <Link href="/loans" className="hover:text-blue-200 transition-colors">Loans</Link>
            <Link href="/about" className="hover:text-blue-200 transition-colors">About Us</Link>
            <Link href="/contact" className="hover:text-blue-200 transition-colors">Contact</Link>
            <div className="flex flex-col space-y-4 pt-4">
               <Link href="/auth/login" className="bg-gray-100 text-blue-800 py-2 px-4 rounded-md font-semibold hover:bg-gray-200 transition-colors text-center">
                Login
              </Link>
              <Link href="/auth/register" className="bg-blue-600 text-white py-2 px-4 rounded-md font-semibold hover:bg-blue-700 transition-colors text-center">
                Open Account
              </Link>
            </div>
          </nav>
        </div>
      )}
    </header>
  );
};

export default Header;