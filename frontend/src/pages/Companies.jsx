import CompanyForm from "../components/CompanyForm";
import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

function Companies() {
  const navigate = useNavigate();
  const { user, isAuthenticated, isLoading } = useAuth();

  useEffect(() => {
    if (isLoading) return; // Wait for auth to load

    if (!isAuthenticated) {
      navigate("/login");
    } else if (
      user?.roles.includes("RESTAURANT_ADMIN") ||
      user?.roles.includes("MANAGER") ||
      user?.roles.includes("USER")
    ) {
      navigate("/unauthorized");
    }
  }, [isAuthenticated, isLoading, user, navigate]);

  if (isLoading) {
    return <div>Loading...</div>;
  }

  return (
    <>
      <div className="flex">
        <div className="bg-stone-200 w-1/6 h-screen">
          <h2 className="bg-darkred text-white text-2xl text-center">Admin Panel</h2>
          <button className="bg-darkred text-white text-xl w-full mt-6 rounded-sm cursor-pointer py-2">Companies</button>
        </div>
        <div className="w-5/6 p-6">
          <CompanyForm />
        </div>
      </div>
    </>
  );
}

export default Companies;