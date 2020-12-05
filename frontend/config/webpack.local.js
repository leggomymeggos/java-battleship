const { merge } = require('webpack-merge');
const baseConfig = require('./webpack.common.js');
const webpack = require('webpack');
const path = require('path');

module.exports = merge(baseConfig, {
    devServer: {
        contentBase: path.resolve(__dirname, "../"),
        inline: true,
        watchContentBase: true,
        compress: true,
        port: 8080
    },

    // Enable sourcemaps for debugging webpack's output.
    devtool: "source-map",
    plugins: [
        new webpack.HotModuleReplacementPlugin(),
        new webpack.DefinePlugin({
            'process.env.NODE_ENV': "\"local\"",
            'process.env.DOMAIN': "\"http://localhost:8081\"",
        }),
    ],

    module: {
        rules: [
            {
                test: /\.(s)css$/,
                use: [{
                    loader: "style-loader" // creates style nodes from JS strings
                }, {
                    loader: "css-loader" // translates CSS into CommonJS
                }, {
                    loader: "sass-loader" // compiles Sass to CSS
                }],
            },
        ]
    },
});