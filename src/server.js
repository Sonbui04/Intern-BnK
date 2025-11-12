import express from "express";
import bodyParser from "body-parser";
import viewEngine from "./config/viewEngine.js"; 
import initWebRoutes from "./route/web.js";
import dotenv from "dotenv";

dotenv.config();

const app = express();

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));


viewEngine(app); 
initWebRoutes(app);

const port = process.env.PORT || 6969;

app.listen(port, () => {
  console.log(` Backend is running at http://localhost:${port}`);
});
