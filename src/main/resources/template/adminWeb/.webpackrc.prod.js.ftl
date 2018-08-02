const webpack = require('webpack');
module.exports = {
    plugins: [
        new webpack.DefinePlugin({
            "serverUrl": JSON.stringify("http://127.0.0.1:8080")
        })
    ]
};