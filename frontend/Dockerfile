FROM node:latest
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
EXPOSE 6969 
CMD ["npm", "run", "dev"] 