import giftcard from './GiftCard.module.css';

function GiftCard() {
  return (
      <div className={giftcard.contentcontainer}>
          <h1 className={giftcard.header}>Gift Cards Now Available</h1>
          <p className={giftcard.paragraph}>Don't forget to pick up a Gift Card from any location. <br>
          </br>Everyone loves a Carolina Wings Gift Card!</p>
      </div>
  );
}

export default GiftCard;