import CompanyForm from "../components/CompanyForm";
import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

function Companies() {
  const navigate = useNavigate();

  useEffect(() => {
    const role = localStorage.getItem("role");

    if (!role) {
      // User is not logged in
      navigate("/login");
    } else if (role !== "ADMIN") {
      // Logged in but wrong role
      navigate("/unauthorized");
    }
  }, [navigate]);

  return (
    <>
      <div>Welcome Admin!</div>
      <CompanyForm />
    </>
  );
}


export default Companies;