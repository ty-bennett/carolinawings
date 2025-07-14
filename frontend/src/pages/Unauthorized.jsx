import NavBar from "../components/NavBar";
import Footer from "../components/Footer";
import { useNavigate } from "react-router-dom";

function Unauthorized() {
  const navigate = useNavigate();

  return(
    <main className="w-full min-h-screen">
      <NavBar />
      <div className="w-full h-full flex flex-col py-5 justify-center items-center mt-2 mb-2">
        <img src="/carolinawings.png" className="mt-4"></img>
        <h1>You are not allowed to view that page</h1>
        <p className="font-oswald">Please return home and try again</p>
        <button onClick={() => navigate("/")} className="mt-5 p-2 bg-darkred mx-6 w-sm text-white text-2xl rounded-md cursor-pointer">Home</button>
      </div>
      <Footer />
    </main>
  )
}

export default Unauthorized;