import React from 'react';
import Link from 'next/link';

const HeroSection = () => {
  return (
    <section className="relative bg-blue-700 text-white py-20 md:py-32">
      {/* Background Image with Overlay */}
      <div 
        className="absolute inset-0 bg-cover bg-center z-0" 
        style={{ backgroundImage: "url('/path/to/your/hero-image.jpg')" }}
      >
        <div className="absolute inset-0 bg-black opacity-50"></div>
      </div>
      
      {/* Content */}
      <div className="container mx-auto px-4 text-center relative z-10">
        <h1 className="text-4xl md:text-6xl font-bold mb-4 leading-tight">
          Secure Banking, Brighter Future
        </h1>
        <p className="text-lg md:text-xl text-blue-100 mb-8 max-w-2xl mx-auto">
          Experience modern banking with personalized services, competitive rates, and the security you can trust.
        </p>
        <Link 
          href="/auth/register" 
          className="bg-white text-blue-800 font-bold py-3 px-8 rounded-full text-lg hover:bg-gray-200 transition-transform transform hover:scale-105 duration-300"
        >
          Open an Account Today
        </Link>
      </div>
    </section>
  );
};

export default HeroSection;