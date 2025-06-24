import NavBar from "../components/NavBar.jsx";
import catering from "../components/Specials.module.css";
import FoodCarousel from "../components/FoodCarousel.jsx";


const Specials = () => {
  return (
    <>
      <main>
        <NavBar />
        <h1 className="bg-[rgb(75,0,0)] mt-2 text-white text-center">Lunch Specials</h1>
        <div className="bg-[rgb(75,0,0)] py-4 text-white text-center mt-10">
          <p className="py-4">Happy Hour 4-7 PM</p>
        </div>
        <div className={catering.carousel}>
          <FoodCarousel />
        </div>
      </main>
    </>
  );
}

export default Specials;
