

'use client'; 

import React, { useState } from 'react';
import Link from 'next/link';
import { useRouter } from 'next/navigation';
import Cookies from 'js-cookie'; // <-- Import the library

const LoginPage = () => {
  const router = useRouter();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState(null);
  const [isLoading, setIsLoading] = useState(false);

  const handleSubmit = async (event) => {
    event.preventDefault();
    setIsLoading(true);
    setError(null);

    try {
      const response = await fetch('http://localhost:8090/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, password }),
      });

      if (!response.ok) {
        throw new Error('Invalid credentials. Please try again.');
      }

      // --- KEY CHANGE IS HERE ---
      const data = await response.json();
      const token = data.data; // Assuming your token is in the 'data' field of the response

      if (token) {
        // Set the cookie. It will expire in 1 day.
        Cookies.set('jwt_token', token, { expires: 1, secure: true, sameSite: 'strict' });
        console.log('Login successful, token stored.');
        
        // Redirect to a protected dashboard page
        router.push('/'); 
      } else {
        throw new Error('Login response did not contain a token.');
      }
      // --- END OF KEY CHANGE ---

    } catch (error) {
      setError(error.message);
    } finally {
      setIsLoading(false);
    }
  };

  // ... rest of your JSX remains the same
   return (
    // Your outer div provided by the (auth)/layout.js
    <div className="w-full max-w-md p-8 space-y-8 bg-white rounded-lg shadow-md">
      <div className="text-center">
        <h1 className="text-3xl font-bold text-gray-800">Welcome Back!</h1>
        <p className="mt-2 text-gray-600">Login to access your account</p>
      </div>

      <form className="mt-8 space-y-6" onSubmit={handleSubmit}>
        <div className="rounded-md shadow-sm -space-y-px">
          <div>
            <input
              id="email-address"
              name="email"
              type="email"
              required
              className="appearance-none rounded-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-t-md focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
              placeholder="Email address"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
          </div>
          <div>
            <input
              id="password"
              name="password"
              type="password"
              required
              className="appearance-none rounded-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-b-md focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
              placeholder="Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
          </div>
        </div>

        <div className="flex items-center justify-end text-sm">
           <Link href="/forgot-password" legacyBehavior>
              <a className="font-medium text-blue-600 hover:text-blue-500">
                Forgot your password?
              </a>
            </Link>
        </div>
        
        {/* Display error message if there is one */}
        {error && <p className="text-sm text-red-600 text-center">{error}</p>}

        <div>
          <button
            type="submit"
            disabled={isLoading}
            className="group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 disabled:bg-blue-400"
          >
            {isLoading ? 'Signing In...' : 'Sign in'}
          </button>
        </div>
      </form>

      <div className="text-center text-sm text-gray-600">
        <p>
          Don't have an account?{' '}
          <Link href="/Register" legacyBehavior>
            <a className="font-medium text-blue-600 hover:text-blue-500">
              Sign up
            </a>
          </Link>
        </p>
      </div>
    </div>
  );
};


export default LoginPage;