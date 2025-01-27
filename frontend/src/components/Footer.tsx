import footer from './Footer.module.css';

function Footer() {
  return (
    <div className={footer.footercontainer}>
      <div className={footer.locationcontainer}>
        <div className={footer.location}>
          <h1 className={footer.header}><u>Ballentine</u></h1>
        </div>
        <div className={footer.location}>
          <h1 className={footer.header}><u>Lexington</u></h1>
        </div>
        <div className={footer.location}>
          <h1 className={footer.header}><u>Cayce</u></h1>
        </div>
        <div className={footer.location}>
          <h1 className={footer.header}><u>Red Bank</u></h1>
        </div>
      </div>
      <div>
        <img src="/carolinawings.png" className={footer.image}></img>
      </div>
    </div>
  );
}

export default Footer;