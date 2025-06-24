import NavBar from "../components/NavBar.jsx";
import catering from "../components/Specials.module.css";
import FoodCarousel from "../components/FoodCarousel.jsx";


const Specials = () => {
  return (
    <>
      <main>
        <NavBar />
        <div className="py-12 mt-2 h-full">
          <h1 className="bg-[rgb(75,0,0)] text-white text-center">Lunch Specials</h1>
        </div>
        <div className="bg-[rgb(75,0,0)] py-2 text-white text-center mt-2">
          <p className="py-4 font-oswald">Happy Hour 4-7 PM</p>
        </div>
        <div className={catering.carousel}>
          <FoodCarousel />
        </div>
        <div>
        </div>
      </main>
    </>
  );
}

export default Specials;
