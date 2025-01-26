import footer from './Footer.module.css';

function Footer() {
  return (
    <div className={footer.footercontainer}>
      <div className={footer.locationcontainer}>
        <div className={footer.location}>
          <h1>Ballentine</h1>
        </div>
        <div className={footer.location}>
          <h1>Lexington</h1>
        </div>
        <div className={footer.location}>
          <h1>Cayce</h1>
        </div>
        <div className={footer.location}>
          <h1>Red Bank</h1>
        </div>
      </div>
    </div>
  );
}

export default Footer;