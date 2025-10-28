'use client';
import React, { useState } from 'react';
import Link from 'next/link';

const ForgotPasswordPage = () => {
  const [email, setEmail] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);
  const [successMessage, setSuccessMessage] = useState('');

  const handleSubmit = async (event) => {
    event.preventDefault();
    setIsLoading(true);
    setError(null);
    setSuccessMessage('');

    try {
      // The API endpoint expects the email as a query parameter
      const response = await fetch(`http://localhost:8090/api/auth/forgot-password?email=${email}`, {
        method: 'POST', // Even with query params, POST is appropriate for actions
        headers: {
          'Content-Type': 'application/json',
        },
      });

      const data = await response.json();

      if (!response.ok) {
        throw new Error(data.message || 'Failed to send reset code.');
      }

      setSuccessMessage(data.message);

    } catch (error) {
      setError(error.message);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="w-full max-w-md p-8 space-y-8 bg-white rounded-lg shadow-md">
      <div className="text-center">
        <h1 className="text-3xl font-bold" style={{ color: '#1E2A78' }}>Forgot Password</h1>
        <p className="mt-2 text-gray-600">Enter your email address to receive a password reset code</p>
      </div>

      <form className="mt-8 space-y-6" onSubmit={handleSubmit}>
        {/* We only show the form if a success message hasn't been sent */}
        {!successMessage && (
          <>
            <div>
              <label htmlFor="email-address" className="text-sm font-medium text-gray-700">Email Address</label>
              <input
                id="email-address"
                name="email"
                type="email"
                required
                className="mt-1 relative block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 sm:text-sm"
                placeholder="Enter your email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
              />
            </div>
            <div>
              <button
                type="submit"
                disabled={isLoading}
                className="w-full flex justify-center py-3 px-4 border border-transparent text-sm font-medium rounded-md text-white disabled:bg-indigo-400"
                style={{ backgroundColor: '#1E2A78' }}
              >
                {isLoading ? 'Sending...' : 'Send Reset Code'}
              </button>
            </div>
          </>
        )}
        
        {error && <p className="text-sm text-red-600 text-center">{error}</p>}
        {successMessage && <p className="text-sm text-green-600 text-center">{successMessage}</p>}
      </form>

      <div className="text-center text-sm text-gray-600 space-y-2">
         <p>
          Remember your Password?{' '}
          <Link href="/Login" legacyBehavior><a className="font-medium text-blue-600 hover:text-blue-500">Login Here</a></Link>
        </p>
      </div>
    </div>
  );
};

export default ForgotPasswordPage;