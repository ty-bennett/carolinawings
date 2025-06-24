/*
  Written by Ty Bennett
*/

import { Carousel } from "@material-tailwind/react";
import { useEffect, useState, useRef } from "react";
import { ChevronLeftIcon, ChevronRightIcon,} from "@heroicons/react/24/solid";

export default function FoodCarousel() {

  return (
    <main>
      <Carousel>
        <img src="/foodimages/bbqsand.jpeg"
        alt="image 1"
        className="h-full w-full object-cover" />
      </Carousel>
    </main>
  );
};


