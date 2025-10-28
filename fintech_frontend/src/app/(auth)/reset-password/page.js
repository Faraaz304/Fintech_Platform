'use client';
import React, { useState } from 'react';
import Link from 'next/link';

const ResetPasswordPage = () => {
  const [resetCode, setResetCode] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');

  const handleSubmit = (event) => {
    event.preventDefault();
    // API call logic will go here
    console.log({
      resetCode,
      newPassword,
      confirmPassword,
    });
  };

  return (
    // This will be wrapped by your (auth)/layout.js for centering
    <div className="w-full max-w-md p-8 space-y-8 bg-white rounded-lg shadow-md">
      
      {/* Header */}
      <div className="text-center">
        <h1 className="text-3xl font-bold text-gray-800" style={{ color: '#1E2A78' }}>
          Reset Password
        </h1>
        <p className="mt-2 text-gray-600">
          Enter your reset code and new password
        </p>
      </div>

      {/* Form */}
      <form className="mt-8 space-y-6" onSubmit={handleSubmit}>
        <div className="space-y-4">
          <div>
            <label htmlFor="reset-code" className="text-sm font-medium text-gray-700">
              Reset Code
            </label>
            <input
              id="reset-code"
              name="resetCode"
              type="text"
              required
              className="mt-1 appearance-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
              placeholder="TN6DR"
              value={resetCode}
              onChange={(e) => setResetCode(e.target.value)}
            />
          </div>
          <div>
            <label htmlFor="new-password" className="text-sm font-medium text-gray-700">
              New Password
            </label>
            <input
              id="new-password"
              name="newPassword"
              type="password"
              required
              className="mt-1 appearance-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
              placeholder="Enter your new password"
              value={newPassword}
              onChange={(e) => setNewPassword(e.target.value)}
            />
          </div>
          <div>
            <label htmlFor="confirm-new-password" className="text-sm font-medium text-gray-700">
              Confirm new password
            </label>
            <input
              id="confirm-new-password"
              name="confirmPassword"
              type="password"
              required
              className="mt-1 appearance-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
              placeholder="Confirm your new password"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
            />
          </div>
        </div>

        <div>
          <button
            type="submit"
            className="group relative w-full flex justify-center py-3 px-4 border border-transparent text-sm font-medium rounded-md text-white focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
            style={{ backgroundColor: '#1E2A78', hover: { backgroundColor: '#141D5A' } }}
          >
            Reset Password
          </button>
        </div>
      </form>

      {/* Footer Links */}
      <div className="text-center text-sm text-gray-600 space-y-2">
        <p>
          Need a new code?{' '}
          <Link href="/forgot-password" legacyBehavior>
            <a className="font-medium text-blue-600 hover:text-blue-500">
              Request another
            </a>
          </Link>
        </p>
         <p>
          Remember your Password?{' '}
          <Link href="/Login" legacyBehavior>
            <a className="font-medium text-blue-600 hover:text-blue-500">
              Login Here
            </a>
          </Link>
        </p>
      </div>
    </div>
  );
};

export default ResetPasswordPage;