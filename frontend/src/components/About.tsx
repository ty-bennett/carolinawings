import about from './About.module.css'

function About() {
  return (
    <div className={about.contentcontainer}>
      <div className={about.headercontainer}>
        <h1 className={about.header}>About Carolina Wings & Rib House</h1>
      </div>
      <div className={about.textcontainer}>
        <img src='/carolinawings.png' alt={"Carolina Wings Logo"}></img>
        <section className={about.abouttext}>
          <p>When the first Carolina Wings & Rib House opened for business in 1996, people soon realized there was something special about this new, local wing joint. 
            As word got out about the great food, personable service, and fun atmosphere, business took off.
          </p>
          <p>  
            Thanks to the support of our friends and neighbors in South Carolina throughout the years, we've been able to grow and expand our business to include four 
            locations in the Midlands. At heart, we are still that local wing joint that set out to provide our customers with delicious food at a great value.
          </p>
          <p>
            Today our menu includes over 20 wing sauces, fresh-cut wing-chips, and a variety of appetizers and entrees that include everything from burgers to 
            salads, sandwiches, ribs, and of course wings. Gift cards are available too, meaning it's easier than ever before to give a gift that's always the perfect fit.
          </p>
          <p>   
            Stop in today and find out first hand what makes Carolina Wings & Rib House the perfect neighborhood bar and grill. We're looking forward to serving you!
          </p>
        </section>
      </div>
    </div>
  );
}


export default About;