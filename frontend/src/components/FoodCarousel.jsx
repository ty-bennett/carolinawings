import { CCarousel, CCarouselItem, CImage } from "@coreui/react";
import carousel from "../components/FoodCarousel.module.css";
import 'boostrap/dist/css/boostrap.min.css';


export const FoodCarousel = () => {
  return (
    <CCarousel controls indicators dark>

      <CCarouselItem>
        <CImage className={carousel.image} src={"foodimages/smokehouse_burger.png"} alt="smokehouse burger" />
      </CCarouselItem>

      <CCarouselItem>
        <CImage className={carousel.image} src={"foodimages/wings and chips.jpg"} alt="wings and wing chips" />
      </CCarouselItem>

      <CCarouselItem>
        <CImage className={carousel.image} src={"foodimages/ribs_with_beer.png"} alt="wings and wing chips" />
      </CCarouselItem>

      <CCarouselItem>
        <CImage className={carousel.image} src={"foodimages/wing_in_ranch.png"} alt="wings and wing chips" />
      </CCarouselItem>

      <CCarouselItem>
        <CImage className={carousel.image} src={"foodimages/wings and chips.jpg"} alt="wings and wing chips" />
      </CCarouselItem>

    </CCarousel>
  );
}

