'use client';
import React, { useState, useEffect, Suspense } from 'react';
import Link from 'next/link';
import { useRouter, useSearchParams } from 'next/navigation';

const ResetPasswordComponent = () => {
  const router = useRouter();
  const searchParams = useSearchParams();

  // State for form inputs and UI feedback
  const [newPassword, setNewPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);
  const [successMessage, setSuccessMessage] = useState('');
  
  // State to hold parameters from the URL
  const [email, setEmail] = useState('');
  const [code, setCode] = useState('');

  // On component load, read the email and code from the URL
  useEffect(() => {
    const emailFromUrl = searchParams.get('email');
    const codeFromUrl = searchParams.get('code');
    
    if (emailFromUrl) setEmail(emailFromUrl);
    if (codeFromUrl) setCode(codeFromUrl);

    if (!emailFromUrl || !codeFromUrl) {
        setError('Invalid reset link. Please request a new one.');
    }
  }, [searchParams]);

  const handleSubmit = async (event) => {
    event.preventDefault();

    if (newPassword !== confirmPassword) {
      setError('Passwords do not match.');
      return;
    }
    if (!email || !code) {
        setError('Invalid or expired reset link.');
        return;
    }

    setIsLoading(true);
    setError(null);
    setSuccessMessage('');

    try {
      const response = await fetch('http://localhost:8090/api/auth/reset-password', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email, code, newPassword }),
      });

      const data = await response.json();
      if (!response.ok) {
        throw new Error(data.message || 'Failed to reset password.');
      }

      setSuccessMessage('Password has been reset successfully! Redirecting to login...');

      // Redirect to login page after a short delay
      setTimeout(() => {
        router.push('/Login');
      }, 3000);

    } catch (error) {
      setError(error.message);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="w-full max-w-md p-8 space-y-8 bg-white rounded-lg shadow-md">
      <div className="text-center">
        <h1 className="text-3xl font-bold" style={{ color: '#1E2A78' }}>Reset Password</h1>
        <p className="mt-2 text-gray-600">Enter your reset code and new password</p>
      </div>

      <form className="mt-8 space-y-6" onSubmit={handleSubmit}>
        <div className="space-y-4">
          <div>
            <label htmlFor="reset-code" className="text-sm font-medium text-gray-700">Reset Code</label>
            <input
              id="reset-code"
              name="resetCode"
              type="text"
              required
              className="mt-1 w-full px-3 py-2 border border-gray-300 rounded-md bg-gray-100"
              value={code} // Value is pre-filled from URL
              readOnly // User should not edit this
            />
          </div>
          <div>
            <label htmlFor="new-password" className="text-sm font-medium text-gray-700">New Password</label>
            <input
              id="new-password"
              name="newPassword"
              type="password"
              required
              className="mt-1 w-full px-3 py-2 border border-gray-300 rounded-md"
              placeholder="Enter your new password"
              value={newPassword}
              onChange={(e) => setNewPassword(e.target.value)}
            />
          </div>
          <div>
            <label htmlFor="confirm-new-password" className="text-sm font-medium text-gray-700">Confirm new password</label>
            <input
              id="confirm-new-password"
              name="confirmPassword"
              type="password"
              required
              className="mt-1 w-full px-3 py-2 border border-gray-300 rounded-md"
              placeholder="Confirm your new password"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
            />
          </div>
        </div>

        <div>
          <button
            type="submit"
            disabled={isLoading || successMessage}
            className="w-full flex justify-center py-3 px-4 border border-transparent text-sm font-medium rounded-md text-white disabled:bg-indigo-400"
            style={{ backgroundColor: '#1E2A78' }}
          >
            {isLoading ? 'Resetting...' : 'Reset Password'}
          </button>
        </div>
        
        {error && <p className="text-sm text-red-600 text-center">{error}</p>}
        {successMessage && <p className="text-sm text-green-600 text-center">{successMessage}</p>}
      </form>
    </div>
  );
};


// In Next.js App Router, using hooks like useSearchParams requires a Suspense boundary.
const ResetPasswordPage = () => (
  <Suspense fallback={<div>Loading...</div>}>
    <ResetPasswordComponent />
  </Suspense>
);


export default ResetPasswordPage;