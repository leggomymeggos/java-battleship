const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = function(app: any) {
    console.log('DOMAIN', process.env.DOMAIN);

    app.use(
        '/api/*',
        createProxyMiddleware({
            target: process.env.DOMAIN || 'http://localhost:8080',
            changeOrigin: true,
        })
    );
};

export {}