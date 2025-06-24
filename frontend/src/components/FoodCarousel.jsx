/*
  Written by Ty Bennett
*/

import Carousel from "react-multi-carousel";
import "react-multi-carousel/lib/styles.css";



const FoodCarousel = () => {

 const responsive = {
        desktop: {
          breakpoint: { max: 3000, min: 1024 },
          items: 3,
          slidesToSlide: 3 // optional, default to 1.
        },
        tablet: {
          breakpoint: { max: 1024, min: 464 },
          items: 2,
          slidesToSlide: 2 // optional, default to 1.
        },
        mobile: {
          breakpoint: { max: 464, min: 0 },
          items: 1,
          slidesToSlide: 1 // optional, default to 1.
        }
      }   

  return (
    <main>
      <Carousel
        swipeable={false}
        centerMode={true}
        draggable={true}
        showDots={true}
        responsive={responsive}
        ssr={true} // means to render carousel on server-side.
        infinite={true}
        autoPlaySpeed={1000}
        keyBoardControl={true}
        customTransition="all .5"
        transitionDuration={500}
        containerClass="carousel-container"
        removeArrowOnDeviceType={["tablet", "mobile"]}
        dotListClass="custom-dot-list-style"
        itemClass="carousel-item-padding-40-px"
      >
        <div className="bg-[rgb(75,0,0)] shadow-md overflow-hidden hover:shadow-xl transition-shadow duration-300"><img src="/foodimages/carousel1.jpg" className="w-full h-full object-cover" /></div>
        <div className="bg-[rgb(75,0,0)] shadow-md overflow-hidden hover:shadow-xl transition-shadow duration-300"><img src="/foodimages/carousel2.png" className="w-full h-full object-cover" /></div>
        <div className="bg-[rgb(75,0,0)] shadow-md overflow-hidden hover:shadow-xl transition-shadow duration-300"><img src="/foodimages/carousel3.png" className="w-full h-full object-cover" /></div>
        <div className="bg-[rgb(75,0,0)] shadow-md overflow-hidden hover:shadow-xl transition-shadow duration-300"><img src="/foodimages/carousel4.png" className="w-full h-full object-cover" /></div>
        <div className="bg-[rgb(75,0,0)] shadow-md overflow-hidden hover:shadow-xl transition-shadow duration-300"><img src="/foodimages/carousel5.jpg" className="w-full h-full object-cover" /></div>
      </Carousel>
    </main>
  );
};

export default FoodCarousel;
