module.exports = {
  content: [
    "./src/**/*.{js,ts,jsx,tsx}", // make sure it scans your files
  ],
  theme: {
    extend: {
      fontFamily: {
        oswald: ['Oswald', 'sans-serif'],       
        opensans: ['Open Sans', 'sans-serif'], 
        bebas: ['Bebas Nueue', 'sans-serif'],   
      },
    },
  },
  plugins: []
} 