// This layout will wrap both your Login and Register pages
export default function AuthLayout({ children }) {
  return (
    <div className="bg-gray-50 min-h-screen flex items-center justify-center">
      {children}
    </div>
  );
}