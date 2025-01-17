//TODO: 
// background image that stays when you scroll (DONE)
// floating white div that scrolls down to location component (DONE)
//  specials div that links to specials page (IP)

import goodeats from './GoodEats.module.css';

function GoodEats() {
    return (
        <div className={goodeats.bgimg}>
            <div className={goodeats.container}>
                <div className={goodeats.floatingdiv}>
                    <div className={goodeats.flexforcontent}>
                        <div>
                            <h1 className={goodeats.goodeatsheading}>Good Eats</h1>
                        </div>
                        <div>
                            <h3 className={goodeats.subheader}>Carolina Wings & Rib House</h3>
                        </div>
                        <div className={goodeats.buttonflex}>
                            <button className={goodeats.chooselocationbutton} type="button">Choose Location</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default GoodEats;