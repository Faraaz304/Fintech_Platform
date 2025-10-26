"use client"
import React from 'react';
import Link from 'next/link';
// Make sure to install react-icons: npm install react-icons
import { FaFacebook, FaTwitter, FaLinkedin, FaInstagram } from 'react-icons/fa';

const Footer = () => {
  return (
    <footer className="bg-blue-900 text-white">
      <div className="container mx-auto px-4 pt-16 pb-8">
        <div className="grid grid-cols-1 md:grid-cols-4 gap-8">
          
          {/* About Section */}
          <div>
            <h3 className="text-xl font-bold mb-4">MyBank</h3>
            <p className="text-blue-200">
              Providing secure and reliable banking solutions to help you achieve your financial goals.
            </p>
          </div>

          {/* Quick Links */}
          <div>
            <h3 className="font-semibold mb-4">Quick Links</h3>
            <ul className="space-y-2">
              <li><Link href="/about" className="text-blue-200 hover:text-white transition-colors">About Us</Link></li>
              <li><Link href="/careers" className="text-blue-200 hover:text-white transition-colors">Careers</Link></li>
              <li><Link href="/locations" className="text-blue-200 hover:text-white transition-colors">Branch & ATM Locator</Link></li>
              <li><Link href="/rates" className="text-blue-200 hover:text-white transition-colors">Rates</Link></li>
            </ul>
          </div>

          {/* Support Section */}
          <div>
            <h3 className="font-semibold mb-4">Support</h3>
            <ul className="space-y-2">
              <li><Link href="/contact" className="text-blue-200 hover:text-white transition-colors">Contact Us</Link></li>
              <li><Link href="/faq" className="text-blue-200 hover:text-white transition-colors">FAQs</Link></li>
              <li><Link href="/security" className="text-blue-200 hover:text-white transition-colors">Security Center</Link></li>
              <li><Link href="/forms" className="text-blue-200 hover:text-white transition-colors">Forms & Documents</Link></li>
            </ul>
          </div>
          
          {/* Social Media & Contact */}
          <div>
            <h3 className="font-semibold mb-4">Connect With Us</h3>
            <div className="flex space-x-4 mb-4">
              <a href="#" target="_blank" rel="noopener noreferrer" className="text-blue-200 hover:text-white transition-colors"><FaFacebook size={24} /></a>
              <a href="#" target="_blank" rel="noopener noreferrer" className="text-blue-200 hover:text-white transition-colors"><FaTwitter size={24} /></a>
              <a href="#" target="_blank" rel="noopener noreferrer" className="text-blue-200 hover:text-white transition-colors"><FaLinkedin size={24} /></a>
              <a href="#" target="_blank" rel="noopener noreferrer" className="text-blue-200 hover:text-white transition-colors"><FaInstagram size={24} /></a>
            </div>
            <p className="text-blue-200">Customer Service: <strong>1-800-123-4567</strong></p>
          </div>
        </div>

        <div className="border-t border-blue-700 mt-8 pt-8">
          <div className="flex flex-col md:flex-row justify-between items-center text-sm text-blue-300">
            <div className="mb-4 md:mb-0">
              <p>&copy; {new Date().getFullYear()} MyBank Corporation. All Rights Reserved.</p>
              <p>Member FDIC. Equal Housing Lender.</p>
            </div>
            <div className="flex space-x-4">
              <Link href="/privacy" className="hover:text-white transition-colors">Privacy Policy</Link>
              <Link href="/terms" className="hover:text-white transition-colors">Terms of Service</Link>
              <Link href="/disclosures" className="hover:text-white transition-colors">Disclosures</Link>
            </div>
          </div>
        </div>
      </div>
    </footer>
  );
};

export default Footer;