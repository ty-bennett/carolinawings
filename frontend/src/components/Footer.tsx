import footer from './Footer.module.css';

function Footer() {
  return (
    <div className={footer.footercontainer}>
      <div className={footer.locationcontainer}>
        <div className={footer.location}>
          <h1 className={footer.header}><u>Ballentine</u></h1>
          <a href="tel:+1803-851-4500"><p className={footer.locationtext}>803-851-4500</p></a>
          <a href="https://www.google.com/maps/dir/?api=1&destination=Carolina+Wings+Ballentine+SC+1000+Marina+Rd+Irmo" target="_blank"><p className={footer.locationtext}>1000 Marina Rd<br>
          </br> Irmo, SC 29063</p></a>   
        </div>
        <div className={footer.location}>
          <h1 className={footer.header}><u>Lexington</u></h1>
          <a href="tel:+1803-356-6244"><p className={footer.locationtext}>803-356-6244</p></a>
          <a href="https://www.google.com/maps/dir/?api=1&destination=Carolina+Wings+Lexington+SC+105+Northpoint+Dr" target="_blank"><p className={footer.locationtext}>105 Northpoint Dr<br>
          </br> Lexington, SC 29072</p></a>
        </div>
        <div className={footer.location}>
          <h1 className={footer.header}><u>Cayce</u></h1>
          <a href="tel:+1803-926-9622"><p className={footer.locationtext}>803-926-9622</p></a>
          <a href="https://www.google.com/maps/dir/?api=1&destination=Carolina+Wings+Cayce+SC+2423+Fish+Hatchery+Rd" target="_blank"><p className={footer.locationtext}>2423 Fish Hatchery Rd <br>
          </br> Cayce, SC 29172</p></a>
        </div>
        <div className={footer.location}>
          <h1 className={footer.header}><u>Red Bank</u></h1>
          <a href="tel:+1803-808-4800"><p className={footer.locationtext}>803-808-0488</p></a>   
          <a href="https://www.google.com/maps/dir/?api=1&destination=Carolina+Wings+Red+Bank+SC+1767+S+Lake+Dr" target="_blank"><p className={footer.locationtext}>1767 S Lake Dr <br>
          </br> Lexington, SC 29073</p></a>
        </div>
      </div>
      <div className={footer.hours}>
        <div className={footer.imagecontainer}>
          <img src="/carolinawings.png" className={footer.image}></img>
          <div className={footer.hourslayout}>
            <h1 className={footer.header}>Hours of Operation</h1>
            <p className={footer.text}>Sunday - Thursday 11AM - 10PM</p>
            <p className={footer.text}>Friday - Saturday 11AM - 11PM</p>
          </div>
        </div>
      </div>
      <div className={footer.bottomlayout}>
        <h1 className={footer.header}>Privacy Policy</h1>
        <div className={footer.followuscontainer}>
          <h1 className={footer.header}>Follow Us</h1>
          <img src="https://img.icons8.com/ios_filled/512/facebook-new.png"className={footer.facebookicon}></img>
        </div>
       
      </div>
    </div>
  );
}

export default Footer;