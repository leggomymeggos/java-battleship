const webpack = require('webpack');
const TerserPlugin = require('terser-webpack-plugin');
const OptimizeCssAssetsPlugin = require('optimize-css-assets-webpack-plugin');
const path = require('path');
const { merge } = require('webpack-merge');
const baseConfig = require('./webpack.common.js');


module.exports = merge(baseConfig, {
    devServer: {
        contentBase: path.resolve(__dirname, "../"),
        inline: true,
        watchContentBase: true,
        compress: true,
        port: 8080
    },
    plugins: [
        new webpack.DefinePlugin({
            'process.env.NODE_ENV': "\"prod\"",
            'process.env.DOMAIN': "\"http://battleship-backend.cfapps.io\"",
        }),
        new OptimizeCssAssetsPlugin({
            assetNameRegExp: /\.*css$/g,
            cssProcessor: require('cssnano'),
            cssProcessorPluginOptions: {
                preset: ['default', {discardComments: {removeAll: true}}],
            },
            canPrint: true
        }),
        // Minify JS
        new TerserPlugin({
            exclude: './node_modules/**',
        }),
        // Minify CSS
        new webpack.LoaderOptionsPlugin({
            minimize: true,
        })
    ],
    module: {
        rules: [
            {
                test: /\.(s)css$/,
                use: [{
                    loader: "style-loader" // creates style nodes from JS strings
                }, {
                    loader: "css-loader", // translates CSS into CommonJS
                }, {
                    loader: "sass-loader" // compiles Sass to CSS
                }],
            },
        ]
    }
});