var pathUtil = require('path');
var Transformer = require('../../').XsltTransformer;
var transformer = new Transformer('html.xsl');

var before = {
    base: __dirname,
    path: 'br.xml',
    contents: '<br/>'
};
var after = transformer.transform(before)[0];

[before, after].forEach(({path, contents}) => {
    console.log(`${pathUtil.basename(path)} | ${contents}`);
});
