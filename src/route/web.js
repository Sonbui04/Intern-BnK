import express from "express";
let router =express.Router();
let initWebRoutes =(app) =>{
    router.get('/',(req,res) =>{
        return res.send('Hello word with IT');
    });
    router.get('/hoi dan it ',(req,res) =>{
        return res.send('hỏi dân it ');
    });




    return app.use("/",router);
};

module.exports =initWebRoutes;