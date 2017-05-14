var pathUtil = require('path');
var java = require('java');

java.classpath.push(pathUtil.resolve(__dirname, 'bridge', 'vendor', 'SaxonHE9-7-0-14J', 'saxon9he.jar'));
java.classpath.push(pathUtil.resolve(__dirname, 'bridge', 'Transformer.jar'));

var JavaXsltTransformer = java.import('Transformer');


class XsltTransformer {

    constructor(xsltPath) {
        try {
            this.javaTransformer = new JavaXsltTransformer(xsltPath);
        } catch (e) {
            var initTransformerError = extractError(e);
            throw initTransformerError;
        }
    }

    transform({base, path, contents}) {
        var results;
        path = pathUtil.resolve(base, path);
        try {
            results = this.javaTransformer.transformSync(base, path, contents);
        } catch (error) {
            var transformError = extractError(error, path);
            throw transformError;
        }
        return convertResults(results);
    }
}

exports.XsltTransformer = XsltTransformer;


function extractError(error, path) {
    if (! error.cause) {
        return error;
    }
    var msg = error.cause.getMessageSync();
    if (path) {
        msg = `Failed to transform ${path}\n${msg}`;
    }
    return new Error(msg);
}

function convertResults(arrayList) {
    var res = [];
    var length = arrayList.sizeSync();
    for (var index = 0; index < length; index++) {
        var {base, path, contents, serializationMethod} = arrayList.getSync(index);
        path = pathUtil.resolve(base, path);
        path = conformResultExtension(path, serializationMethod);
        res.push({base, path, contents});
    }
    return res;
}

function conformResultExtension(path, serializationMethod) {
    switch (serializationMethod) {
        case 'xhtml': return changeExtension(path, '.xhtml');
        case 'html' : return changeExtension(path, '.html');
        case 'text' : return changeExtension(path, '.txt');
        default     : return path;
    }
}

function changeExtension(path, newExt) {
    var {dir, name} = pathUtil.parse(path);
    return pathUtil.format({dir, name, ext: newExt})
}
