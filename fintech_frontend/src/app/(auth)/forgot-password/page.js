'use client';
import React, { useState } from 'react';
import Link from 'next/link';

const ForgotPasswordPage = () => {
  const [email, setEmail] = useState('');

  const handleSubmit = (event) => {
    event.preventDefault();
    // API call logic will go here
    console.log('Requesting password reset for:', email);
  };

  return (
    // This will be wrapped by your (auth)/layout.js for centering
    <div className="w-full max-w-md p-8 space-y-8 bg-white rounded-lg shadow-md">
      
      {/* Header */}
      <div className="text-center">
        <h1 className="text-3xl font-bold text-gray-800" style={{ color: '#1E2A78' }}>
          Forgot Password
        </h1>
        <p className="mt-2 text-gray-600">
          Enter your email address to receive a password reset code
        </p>
      </div>

      {/* Form */}
      <form className="mt-8 space-y-6" onSubmit={handleSubmit}>
        <div className="space-y-2">
           <div>
            <label htmlFor="email-address" className="text-sm font-medium text-gray-700">
              Email Address
            </label>
            <input
              id="email-address"
              name="email"
              type="email"
              autoComplete="email"
              required
              className="mt-1 appearance-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
              placeholder="Enter your email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
          </div>
        </div>

        <div>
          <button
            type="submit"
            className="group relative w-full flex justify-center py-3 px-4 border border-transparent text-sm font-medium rounded-md text-white focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
            style={{ backgroundColor: '#1E2A78', hover: { backgroundColor: '#141D5A' } }}
          >
            Send Reset Code
          </button>
        </div>
      </form>

      {/* Footer Links */}
      <div className="text-center text-sm text-gray-600 space-y-2">
        <p>
          Remember your Password?{' '}
          <Link href="/Login" legacyBehavior>
            <a className="font-medium text-blue-600 hover:text-blue-500">
              Login Here
            </a>
          </Link>
        </p>
        <p>
          Don't have an account?{' '}
          <Link href="/Register" legacyBehavior>
            <a className="font-medium text-blue-600 hover:text-blue-500">
              Sign Up
            </a>
          </Link>
        </p>
      </div>
    </div>
  );
};

export default ForgotPasswordPage;