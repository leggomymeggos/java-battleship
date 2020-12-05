module.exports = (env) => {
    return require(`./config/webpack.${env.NODE_ENV}.js`)
};
