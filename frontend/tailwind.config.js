module.exports = {
  content: [
    "./src/**/*.{js,ts,jsx,tsx}", // make sure it scans your files
  ],
  theme: {
    extend: {
      fontFamily: {
        oswald: ['Oswald', 'sans-serif'],       
        opensans: ['Open Sans', 'sans-serif'], 
        bebas: ['Bebas Neue', 'sans-serif']   
      },
      colors: {
        darkred: 'rgb(75,0,0)', 
        yellow: 'rgb(212, 189, 118)'
      },
    },
  },
  plugins: []
} 