//TODO: 
// background image that stays when you scroll
// floating white div that scrolls down to location component
//  specials div that links to specials page
// balls

import goodeats from './GoodEats.module.css';

function GoodEats() {
    return (
        <div className={goodeats.bgimg}>
            <div className={goodeats.container}>
                <div className={goodeats.floatingdiv}>
                    <div>
                        <h1 className={goodeats.goodeatsheading}>Good Eats</h1>
                    </div>
                    <div>
                        <h3 className={goodeats.subheader}>Carolina Wings & Rib House</h3>
                    </div>
                    <button className={goodeats.chooselocationbutton}>Choose Location</button>
                </div>
            </div>
        </div>
    )
}

export default GoodEats;