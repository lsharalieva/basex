package org.basex.query.func;

import static org.basex.query.QueryTokens.*;
import static org.basex.query.item.SeqType.*;
import org.basex.query.expr.Expr;
import org.basex.query.item.SeqType;
import org.basex.util.InputInfo;
import org.basex.util.Reflect;

/**
 * Signatures of all XQuery functions.
 *
 * @author BaseX Team 2005-11, BSD License
 * @author Christian Gruen
 */
public enum FunDef {

  /* FNAcc functions. */

  /** XQuery function. */
  POS(FNURI, FNAcc.class, 0, 0, "position()", ITR),
  /** XQuery function. */
  LAST(FNURI, FNAcc.class, 0, 0, "last()", ITR),
  /** XQuery function. */
  STRING(FNURI, FNAcc.class, 0, 1, "string([item])", STR),
  /** XQuery function. */
  NUMBER(FNURI, FNAcc.class, 0, 1, "number([item])", ITR),
  /** XQuery function. */
  STRLEN(FNURI, FNAcc.class, 0, 1, "string-length([item])", ITR),
  /** XQuery function. */
  NORM(FNURI, FNAcc.class, 0, 1, "normalize-space([string])", STR),
  /** XQuery function. */
  URIQNAME(FNURI, FNAcc.class, 1, 1, "namespace-uri-from-QName(qname)", URI_ZO),

  /* FNAggr functions. */

  /** XQuery function. */
  AVG(FNURI, FNAggr.class, 1, 1, "avg(item)", ITEM_ZO),
  /** XQuery function. */
  COUNT(FNURI, FNAggr.class, 1, 1, "count(item)", ITR),
  /** XQuery function. */
  MAX(FNURI, FNAggr.class, 1, 2, "max(item[,coll])", ITEM_ZO),
  /** XQuery function. */
  MIN(FNURI, FNAggr.class, 1, 2, "min(item[,coll])", ITEM_ZO),
  /** XQuery function. */
  SUM(FNURI, FNAggr.class, 1, 2, "sum(item[,zero])", ITEM_ZO),

  /* FNContext functions. */

  /** XQuery function. */
  CURRDATE(FNURI, FNContext.class, 0, 0, "current-date()", DAT),
  /** XQuery function. */
  CURRDTM(FNURI, FNContext.class, 0, 0, "current-dateTime()", DAT),
  /** XQuery function. */
  CURRTIME(FNURI, FNContext.class, 0, 0, "current-time()", DAT),
  /** XQuery function. */
  IMPLZONE(FNURI, FNContext.class, 0, 0, "implicit-timezone()", DAT),
  /** XQuery function. */
  COLLAT(FNURI, FNContext.class, 0, 0, "default-collation()", STR),
  /** XQuery function. */
  STBASEURI(FNURI, FNContext.class, 0, 0, "static-base-uri()", URI_ZO),

  /* FNDate functions. */

  /** XQuery function. */
  DAYDAT(FNURI, FNDate.class, 1, 1, "day-from-date(item)", ITR_ZO),
  /** XQuery function. */
  DAYDTM(FNURI, FNDate.class, 1, 1, "day-from-dateTime(datetime)", ITR_ZO),
  /** XQuery function. */
  DAYDUR(FNURI, FNDate.class, 1, 1, "days-from-duration(dur)", ITR_ZO),
  /** XQuery function. */
  HOUDTM(FNURI, FNDate.class, 1, 1, "hours-from-dateTime(datetime)", ITR_ZO),
  /** XQuery function. */
  HOUDUR(FNURI, FNDate.class, 1, 1, "hours-from-duration(dur)", ITR_ZO),
  /** XQuery function. */
  HOUTIM(FNURI, FNDate.class, 1, 1, "hours-from-time(item)", ITR_ZO),
  /** XQuery function. */
  MINDTM(FNURI, FNDate.class, 1, 1, "minutes-from-dateTime(datetime)", ITR_ZO),
  /** XQuery function. */
  MINDUR(FNURI, FNDate.class, 1, 1, "minutes-from-duration(dur)", ITR_ZO),
  /** XQuery function. */
  MINTIM(FNURI, FNDate.class, 1, 1, "minutes-from-time(item)", ITR_ZO),
  /** XQuery function. */
  MONDAT(FNURI, FNDate.class, 1, 1, "month-from-date(item)", ITR_ZO),
  /** XQuery function. */
  MONDTM(FNURI, FNDate.class, 1, 1, "month-from-dateTime(datetime)", ITR_ZO),
  /** XQuery function. */
  MONDUR(FNURI, FNDate.class, 1, 1, "months-from-duration(dur)", ITR_ZO),
  /** XQuery function. */
  SECDTM(FNURI, FNDate.class, 1, 1, "seconds-from-dateTime(datetime)", ITR_ZO),
  /** XQuery function. */
  SECDUR(FNURI, FNDate.class, 1, 1, "seconds-from-duration(dur)", ITR_ZO),
  /** XQuery function. */
  SECTIM(FNURI, FNDate.class, 1, 1, "seconds-from-time(item)", ITR_ZO),
  /** XQuery function. */
  ZONDAT(FNURI, FNDate.class, 1, 1, "timezone-from-date(item)", DAT_ZM),
  /** XQuery function. */
  ZONDTM(FNURI, FNDate.class, 1, 1, "timezone-from-dateTime(item)", DAT_ZM),
  /** XQuery function. */
  ZONTIM(FNURI, FNDate.class, 1, 1, "timezone-from-time(item)", DAT_ZM),
  /** XQuery function. */
  YEADAT(FNURI, FNDate.class, 1, 1, "year-from-date(item)", ITR_ZO),
  /** XQuery function. */
  YEADTM(FNURI, FNDate.class, 1, 1, "year-from-dateTime(datetime)", ITR_ZO),
  /** XQuery function. */
  YEADUR(FNURI, FNDate.class, 1, 1, "years-from-duration(dur)", ITR_ZO),
  /** XQuery function. */
  DATZON(FNURI, FNDate.class, 1, 2, "adjust-date-to-timezone(date[,zone])",
      DAT_ZM),
  /** XQuery function. */
  DTMZON(FNURI, FNDate.class, 1, 2, "adjust-dateTime-to-timezone(date[,zone])",
      DAT_ZM),
  /** XQuery function. */
  TIMZON(FNURI, FNDate.class, 1, 2, "adjust-time-to-timezone(date[,zone])",
      DAT_ZM),
  /** XQuery function. */
  DATETIME(FNURI, FNDate.class, 2, 2, "dateTime(date,time)", DAT_ZM),

  /* FNFormat functions. */

  /** XQuery function. */
  FORMINT(FNURI, FNFormat.class, 2, 3,
      "format-integer(number,picture[,lang])", STR),
  /** XQuery function. */
  FORMNUM(FNURI, FNFormat.class, 2, 3,
      "format-number(number,picture[,format])", STR),
  /** XQuery function. */
  FORMDTM(FNURI, FNFormat.class, 2, 5,
      "format-dateTime(number,picture,[lang[,cal[,place]]])", STR),
  /** XQuery function. */
  FORMDAT(FNURI, FNFormat.class, 2, 5,
      "format-date(number,picture,[lang[,cal[,place]]])", STR),
  /** XQuery function. */
  FORMTIM(FNURI, FNFormat.class, 2, 5,
      "format-time(number,picture,[lang[,cal[,place]]])", STR),

  /* FNFunc functions. */

  /** XQuery function. */
  FILTER(FNURI, FNFunc.class, 2, 2, "filter(function,seq)", ITEM_ZM),
  /** XQuery function. */
  FUNCNAME(FNURI, FNFunc.class, 1, 1, "function-name(function)", STR),
  /** XQuery function. */
  FUNCARITY(FNURI, FNFunc.class, 1, 1, "function-arity(function)", ITR),
  /** XQuery function. */
  MAP(FNURI, FNFunc.class, 2, 2, "map(function,seq)", ITEM_ZM),
  /** XQuery function. */
  MAPPAIRS(FNURI, FNFunc.class, 3, 3, "map-pairs(function,seq1,seq2)", ITEM_ZM),
  /** XQuery function. */
  FOLDLEFT(FNURI, FNFunc.class, 3, 3, "fold-left(function,zero,seq)", ITEM_ZM),
  /** XQuery function. */
  FOLDRIGHT(FNURI, FNFunc.class, 3, 3, "fold-right(function,zero,seq)",
      ITEM_ZM),

  /* FNGen functions. */

  /** XQuery function. */
  DATA(FNURI, FNGen.class, 0, 1, "data([item])", ITEM_ZM),
  /** XQuery function. */
  COLL(FNURI, FNGen.class, 0, 1, "collection([uri])", NOD_ZM),
  /** XQuery function. */
  DOC(FNURI, FNGen.class, 1, 1, "doc(uri)", NOD_ZO),
  /** XQuery function. */
  DOCAVL(FNURI, FNGen.class, 1, 1, "doc-available(uri)", BLN),
  /** XQuery function. */
  PUT(FNURI, FNGen.class, 2, 2, "put(node,path)", ITEM_ZM),
  /** XQuery function. */
  PARSETXT(FNURI, FNGen.class, 1, 2, "unparsed-text(uri[,encoding])", STR_ZO),
  /** XQuery function. */
  PARSETXTLIN(FNURI, FNGen.class, 1, 2,
      "unparsed-text-lines(uri[,encoding])", STR_ZM),
  /** XQuery function. */
  PARSETXTAVL(FNURI, FNGen.class, 1, 2,
      "unparsed-text-available(uri[,encoding])", BLN),
  /** XQuery function. */
  PARSEXML(FNURI, FNGen.class, 1, 2, "parse-xml(string[,base])", NOD),
  /** XQuery function. */
  URICOLL(FNURI, FNGen.class, 0, 1, "uri-collection([uri])", URI_ZM),
  /** XQuery function. */
  SERIALIZE(FNURI, FNGen.class, 1, 2, "serialize(node[,params])", NOD),

  /* FNId functions. */

  /** XQuery function. */
  ID(FNURI, FNId.class, 1, 2, "id(string[,item])", NOD_ZM),
  /** XQuery function. */
  IDREF(FNURI, FNId.class, 1, 2, "idref(string[,item])", NOD_ZM),
  /** XQuery function. */
  LANG(FNURI, FNId.class, 1, 2, "lang(string[,item])", BLN),
  /** XQuery function. */
  ELID(FNURI, FNId.class, 1, 2, "element-with-id(string[,item])", NOD_ZM),

  /* FNInfo functions. */

  /** XQuery function. */
  ERROR(FNURI, FNInfo.class, 0, 3, "error([code[,desc[,object]]])", ITEM_ZM),
  /** XQuery function. */
  TRACE(FNURI, FNInfo.class, 2, 2, "trace(item,message)", ITEM_ZM),
  /** XQuery function. */
  ENV(FNURI, FNInfo.class, 1, 1, "environment-variable(string)", STR_ZO),
  /** XQuery function. */
  ENVS(FNURI, FNInfo.class, 0, 0, "available-environment-variables()", STR_ZM),

  /* FNNode functions. */

  /** XQuery function. */
  DOCURI(FNURI, FNNode.class, 0, 1, "document-uri([node])", URI_ZO),
  /** XQuery function. */
  NILLED(FNURI, FNNode.class, 1, 1, "nilled(node)", BLN_ZO),
  /** XQuery function. */
  NODENAME(FNURI, FNNode.class, 0, 1, "node-name([node])", QNM_ZO),
  /** XQuery function. */
  LOCNAME(FNURI, FNNode.class, 0, 1, "local-name([node])", STR),
  /** XQuery function. */
  NAME(FNURI, FNNode.class, 0, 1, "name([node])", STR),
  /** XQuery function. */
  NSURI(FNURI, FNNode.class, 0, 1, "namespace-uri([node])", URI),
  /** XQuery function. */
  ROOT(FNURI, FNNode.class, 0, 1, "root([node])", NOD_ZO),
  /** XQuery function. */
  BASEURI(FNURI, FNNode.class, 0, 1, "base-uri([node])", URI_ZO),
  /** XQuery function. */
  GENID(FNURI, FNNode.class, 0, 1, "generate-id([node])", STR),
  /** XQuery function. */
  CHILDREN(FNURI, FNNode.class, 1, 1, "has-children(node)", BLN),

  /* FNNum functions. */

  /** XQuery function. */
  ABS(FNURI, FNNum.class, 1, 1, "abs(num)", ITR_ZO),
  /** XQuery function. */
  CEIL(FNURI, FNNum.class, 1, 1, "ceiling(num)", ITR_ZO),
  /** XQuery function. */
  FLOOR(FNURI, FNNum.class, 1, 1, "floor(num)", ITR_ZO),
  /** XQuery function. */
  ROUND(FNURI, FNNum.class, 1, 2, "round(num[,prec])", ITR_ZO),
  /** XQuery function. */
  RNDHLF(FNURI, FNNum.class, 1, 2, "round-half-to-even(num[,prec])", ITR_ZO),

  /* FNPat functions. */

  /** XQuery function. */
  MATCH(FNURI, FNPat.class, 2, 3, "matches(item,pattern[,mod])", BLN),
  /** XQuery function. */
  REPLACE(FNURI, FNPat.class, 3, 4, "replace(item,pattern,replace[,mod])",
      STR),
  /** XQuery function. */
  TOKEN(FNURI, FNPat.class, 2, 3, "tokenize(item,pattern[,mod])", STR_ZM),
  /** XQuery function. */
  ANALZYE(FNURI, FNPat.class, 2, 3, "analyze-string(input,pattern[,mod])",
      NOD),

  /* FNQName functions. */

  /** XQuery function. */
  INSCOPE(FNURI, FNQName.class, 1, 1, "in-scope-prefixes(elem)", STR_ZM),
  /** XQuery function. */
  LOCNAMEQNAME(FNURI, FNQName.class, 1, 1, "local-name-from-QName(qname)",
      STR_ZO),
  /** XQuery function. */
  NSURIPRE(FNURI, FNQName.class, 2, 2, "namespace-uri-for-prefix(pref,elem)",
      URI_ZO),
  /** XQuery function. */
  QNAME(FNURI, FNQName.class, 2, 2, "QName(uri,name)", QNM),
  /** XQuery function. */
  PREQNAME(FNURI, FNQName.class, 1, 1, "prefix-from-QName(qname)", STR_ZO),
  /** XQuery function. */
  RESQNAME(FNURI, FNQName.class, 2, 2, "resolve-QName(item,base)", QNM_ZO),
  /** XQuery function. */
  RESURI(FNURI, FNQName.class, 1, 2, "resolve-uri(name[,elem])", URI_ZO),

  /* FNSeq functions. */

  /** XQuery function. */
  DISTINCT(FNURI, FNSeq.class, 1, 2, "distinct-values(items[,coll])", ITEM_ZM),
  /** XQuery function. */
  INDEXOF(FNURI, FNSeq.class, 2, 3, "index-of(items,item[,coll])", ITR_ZM),
  /** XQuery function. */
  INSBEF(FNURI, FNSeq.class, 3, 3, "insert-before(items,pos,insert)",
      ITEM_ZM),
  /** XQuery function. */
  REMOVE(FNURI, FNSeq.class, 2, 2, "remove(items,pos)", ITEM_ZM),
  /** XQuery function. */
  REVERSE(FNURI, FNSeq.class, 1, 1, "reverse(items)", ITEM_ZM),
  /** XQuery function. */
  SUBSEQ(FNURI, FNSeq.class, 2, 3, "subsequence(items,start[,len])", ITEM_ZM),
  /** XQuery function. */
  HEAD(FNURI, FNSeq.class, 1, 1, "head(items)", ITEM_ZO),
  /** XQuery function. */
  TAIL(FNURI, FNSeq.class, 1, 1, "tail(items)", ITEM_ZM),
  /** XQuery function. */
  OUTERMOST(FNURI, FNSeq.class, 1, 1, "outermost(nodes)", NOD_ZM),
  /** XQuery function. */
  INNERMOST(FNURI, FNSeq.class, 1, 1, "innermost(nodes)", NOD_ZM),

  /* FNSimple functions. */

  /** XQuery function. */
  FALSE(FNURI, FNSimple.class, 0, 0, "false()", BLN),
  /** XQuery function. */
  TRUE(FNURI, FNSimple.class, 0, 0, "true()", BLN),
  /** XQuery function. */
  BOOLEAN(FNURI, FNSimple.class, 1, 1, "boolean(item)", BLN),
  /** XQuery function. */
  NOT(FNURI, FNSimple.class, 1, 1, "not(item)", BLN),
  /** XQuery function. */
  EMPTY(FNURI, FNSimple.class, 1, 1, "empty(item)", BLN),
  /** XQuery function. */
  EXISTS(FNURI, FNSimple.class, 1, 1, "exists(item)", BLN),
  /** XQuery function. */
  UNORDER(FNURI, FNSimple.class, 1, 1, "unordered(item)", ITEM_ZM),
  /** XQuery function. */
  ZEROORONE(FNURI, FNSimple.class, 1, 1, "zero-or-one(item)", ITEM_ZO),
  /** XQuery function. */
  EXACTLYONE(FNURI, FNSimple.class, 1, 1, "exactly-one(item)", ITEM),
  /** XQuery function. */
  ONEORMORE(FNURI, FNSimple.class, 1, 1, "one-or-more(item)", ITEM_OM),
  /** XQuery function. */
  DEEPEQUAL(FNURI, FNSimple.class, 2, 3, "deep-equal(item,item[,coll])", BLN),

  /* FNStr functions. */

  /** XQuery function. */
  CODEPNT(FNURI, FNStr.class, 2, 2, "codepoint-equal(string,string)", BLN_ZO),
  /** XQuery function. */
  CODESTR(FNURI, FNStr.class, 1, 1, "codepoints-to-string(nums)", STR),
  /** XQuery function. */
  COMPARE(FNURI, FNStr.class, 2, 3, "compare(first,second[,coll])", ITR_ZO),
  /** XQuery function. */
  CONCAT(FNURI, FNStr.class, 2, Integer.MAX_VALUE,
      "concat(string,string[,...])", STR),
  /** XQuery function. */
  CONTAINS(FNURI, FNStr.class, 2, 3, "contains(string,sub[,coll])", BLN),
  /** XQuery function. */
  ENCURI(FNURI, FNStr.class, 1, 1, "encode-for-uri(string)", STR),
  /** XQuery function. */
  ENDS(FNURI, FNStr.class, 2, 3, "ends-with(string,sub[,coll])", BLN),
  /** XQuery function. */
  ESCURI(FNURI, FNStr.class, 1, 1, "escape-html-uri(string)", STR),
  /** XQuery function. */
  IRIURI(FNURI, FNStr.class, 1, 1, "iri-to-uri(string)", STR),
  /** XQuery function. */
  LOWER(FNURI, FNStr.class, 1, 1, "lower-case(string)", STR),
  /** XQuery function. */
  NORMUNI(FNURI, FNStr.class, 1, 2, "normalize-unicode(string[,form])", STR),
  /** XQuery function. */
  STARTS(FNURI, FNStr.class, 2, 3, "starts-with(string,sub[,coll])", BLN),
  /** XQuery function. */
  STRJOIN(FNURI, FNStr.class, 1, 2, "string-join(strings[,sep])", STR),
  /** XQuery function. */
  STCODE(FNURI, FNStr.class, 1, 1, "string-to-codepoints(string)", ITR_ZM),
  /** XQuery function. */
  SUBSTR(FNURI, FNStr.class, 2, 3, "substring(string,start[,len])", STR),
  /** XQuery function. */
  SUBAFTER(FNURI, FNStr.class, 2, 3, "substring-after(string,sub[,coll])",
      STR),
  /** XQuery function. */
  SUBBEFORE(FNURI, FNStr.class, 2, 3, "substring-before(string,sub[,coll])",
      STR),
  /** XQuery function. */
  TRANS(FNURI, FNStr.class, 3, 3, "translate(strings,map,trans)", STR),
  /** XQuery function. */
  UPPER(FNURI, FNStr.class, 1, 1, "upper-case(string)", STR),

  /* FNMath functions. */

  /** XQuery math function. */
  PI(MATHURI, FNMath.class, 0, 0, "pi()", ITR),
  /** XQuery math function. */
  SQRT(MATHURI, FNMath.class, 1, 1, "sqrt(number)", ITR_ZO),
  /** XQuery math function. */
  SIN(MATHURI, FNMath.class, 1, 1, "sin(number)", ITR_ZO),
  /** XQuery math function. */
  COS(MATHURI, FNMath.class, 1, 1, "cos(number)", ITR_ZO),
  /** XQuery math function. */
  TAN(MATHURI, FNMath.class, 1, 1, "tan(number)", ITR_ZO),
  /** XQuery math function. */
  ASIN(MATHURI, FNMath.class, 1, 1, "asin(number)", ITR_ZO),
  /** XQuery math function. */
  ACOS(MATHURI, FNMath.class, 1, 1, "acos(number)", ITR_ZO),
  /** XQuery math function. */
  ATAN(MATHURI, FNMath.class, 1, 1, "atan(number)", ITR_ZO),
  /** XQuery math function. */
  ATAN2(MATHURI, FNMath.class, 1, 1, "atan2(number,number)", ITR_ZO),
  /** XQuery math function. */
  POW(MATHURI, FNMath.class, 2, 2, "pow(number,nummber)", ITR_ZO),
  /** XQuery math function. */
  EXP(MATHURI, FNMath.class, 1, 1, "exp(number)", ITR_ZO),
  /** XQuery math function. */
  EXP10(MATHURI, FNMath.class, 1, 1, "exp10(number)", ITR_ZO),
  /** XQuery math function. */
  LOG(MATHURI, FNMath.class, 1, 1, "log(number)", ITR_ZO),
  /** XQuery math function. */
  LOG10(MATHURI, FNMath.class, 1, 1, "log10(number)", ITR_ZO),

  /** XQuery math function (project specific). */
  RAND(MATHURI, FNMath.class, 0, 0, "random()", ITR),
  /** XQuery math function (project specific). */
  E(MATHURI, FNMath.class, 0, 0, "e()", ITR),
  /** XQuery math function (project specific). */
  SINH(MATHURI, FNMath.class, 1, 1, "sinh(number)", ITR_ZO),
  /** XQuery math function (project specific). */
  COSH(MATHURI, FNMath.class, 1, 1, "cosh(number)", ITR_ZO),
  /** XQuery math function (project specific). */
  TANH(MATHURI, FNMath.class, 1, 1, "tanh(number)", ITR_ZO),

  /* FNFile functions */

  /** XQuery function */
  FEXISTS(FILEURI, FNFile.class, 1, 1, "exists(path)", BLN),
  /** XQuery function */
  ISDIR(FILEURI, FNFile.class, 1, 1, "is-directory(path)", BLN),
  /** XQuery function */
  ISFILE(FILEURI, FNFile.class, 1, 1, "is-file(path)", BLN),
  /** XQuery function */
  ISREAD(FILEURI, FNFile.class, 1, 1, "is-readable(path)", BLN),
  /** XQuery function */
  ISWRITE(FILEURI, FNFile.class, 1, 1, "is-writable(path)", BLN),
  /** XQuery function */
  LASTMOD(FILEURI, FNFile.class, 1, 1, "last-modified(path)", DAT),
  /** XQuery function */
  SIZE(FILEURI, FNFile.class, 1, 1, "size(path)", ITR),
  /** XQuery function */
  FLIST(FILEURI, FNFile.class, 1, 3,
      "list(path[,recursive[,pattern]])", STR_ZM),
  /** XQuery function */
  PATHSEP(FILEURI, FNFile.class, 0, 0, "path-separator()", STR),
  /** XQuery function */
  PATHTOFULL(FILEURI, FNFile.class, 1, 1, "path-to-full-path(path)", STR),
  /** XQuery function */
  PATHTOURI(FILEURI, FNFile.class, 1, 1, "path-to-uri(path)", URI),
  /** XQuery function */
  CREATEDIR(FILEURI, FNFile.class, 1, 1, "create-directory(path)", ITEM_Z),
  /** XQuery function */
  DELETE(FILEURI, FNFile.class, 1, 2, "delete(path[,recursive])", ITEM_Z),
  /** XQuery function */
  READ(FILEURI, FNFile.class, 1, 2, "read(path[,encoding])", STR),
  /** XQuery function */
  READBIN(FILEURI, FNFile.class, 1, 1, "read-binary(path)", B64),
  /** XQuery function */
  WRITE(FILEURI, FNFile.class, 2, 4,
      "write(path,data[,params[,append]])", ITEM_Z),
  /** XQuery function */
  WRITEBIN(FILEURI, FNFile.class, 2, 3,
      "write-binary(path,base64[,append])", ITEM_Z),
  /** XQuery function */
  COPY(FILEURI, FNFile.class, 2, 2, "copy(source,target)", ITEM_Z),
  /** XQuery function */
  MOVE(FILEURI, FNFile.class, 2, 2, "move(source,target)", ITEM_Z),

  /* FNZIP functions */

  /** XQuery function */
  BENTRY(ZIPURI, FNZip.class, 2, 2, "binary-entry(path,entry)", B64),
  /** XQuery function */
  TEXTENTRY(ZIPURI, FNZip.class, 2, 3,
      "text-entry(path,entry[,encoding])", STR),
  /** XQuery function */
  HTMLENTRY(ZIPURI, FNZip.class, 2, 2, "html-entry(path,entry)", NOD),
  /** XQuery function */
  XMLENTRY(ZIPURI, FNZip.class, 2, 2, "xml-entry(path,entry)", NOD),
  /** XQuery function */
  ENTRIES(ZIPURI, FNZip.class, 1, 1, "entries(path)", NOD),
  /** XQuery function */
  ZIPFILE(ZIPURI, FNZip.class, 1, 1, "zip-file(zip)", ITEM_Z),
  /** XQuery function */
  UPDATE(ZIPURI, FNZip.class, 2, 2, "update-entries(zip,output)", ITEM_Z),

  /* FNHttp functions */

  /** XQuery function */
  SENDREQUEST(HTTPURI, FNHttp.class, 1, 2,
      "send-request(request[,href])", ITEM_OM),

  /* FNDb functions */

  /** Database function: opens a database. */
  OPEN(DBURI, FNDb.class, 1, 1, "open(string)", NOD_ZM),
  /** Database function: opens a specific database node. */
  OPENPRE(DBURI, FNDb.class, 2, 2, "open-pre(string,pre)", NOD_ZM),
  /** Database function: opens a specific database node. */
  OPENID(DBURI, FNDb.class, 2, 2, "open-id(string,id)", NOD_ZM),
  /** Database function: searches the text index. */
  TEXT(DBURI, FNDb.class, 1, 1, "text(string)", NOD_ZM),
  /** Database function: searches the attribute index. */
  ATTR(DBURI, FNDb.class, 1, 2, "attribute(string[,name])", NOD_ZM),
  /** Database function: searches the full-text index. */
  FULLTEXT(DBURI, FNDb.class, 1, 1, "fulltext(string)", NOD_ZM),
  /** Database function: lists all database. */
  LIST(DBURI, FNDb.class, 0, 0, "list()", STR_ZM),
  /** Database function: lists system information. */
  SYSTEM(DBURI, FNDb.class, 0, 0, "system()", STR),
  /** Database function: returns database or index information. */
  INFO(DBURI, FNDb.class, 0, 1, "info([type])", STR),
  /** Database function: returns the node ids of database nodes. */
  NODEID(DBURI, FNDb.class, 1, 1, "node-id(nodes)", ITR_ZM),
  /** Database function: returns the pre values of database nodes. */
  NODEPRE(DBURI, FNDb.class, 1, 1, "node-pre(nodes)", ITR_ZM),

  /* FNFt functions */

  /** Database function: searches the full-text index. */
  SEARCH(FTURI, FNFt.class, 2, 2, "search(node,string)", NOD_ZM),
  /** Database function: marks the hits of a full-text request. */
  MARK(FTURI, FNFt.class, 1, 2, "mark(nodes[,tag])", NOD_ZM),
  /** Database function: returns the full-text score. */
  SCORE(FTURI, FNFt.class, 1, 1, "score(items)", ITR_ZM),
  /** Database function: extracts full-text results. */
  EXTRACT(FTURI, FNFt.class, 1, 3, "extract(items[,tag[,length]])", NOD_ZM),

  /* FNUtil functions. */

  /** Utility function: evaluates the specified query. */
  EVAL(UTILURI, FNUtil.class, 1, 1, "eval(string)", ITEM_ZM),
  /** Utility function: evaluates the specified query file. */
  RUN(UTILURI, FNUtil.class, 1, 1, "run(string)", ITEM_ZM),
  /** Utility function: formats a string using the printf syntax. */
  FORMAT(UTILURI, FNUtil.class, 2, Integer.MAX_VALUE,
      "format(format,item1[,...])", STR),
  /** Utility function: returns the memory consumption in mb. */
  MB(UTILURI, FNUtil.class, 1, 2, "mb(expr[,cache])", STR),
  /** Utility function: measures the execution time of an expression. */
  MS(UTILURI, FNUtil.class, 1, 2, "ms(expr[,cache])", STR),
  /** Utility function: converts a number to a given base. */
  TO_BASE(UTILURI, FNUtil.class, 2, 2, "integer-to-base(num,base)", STR),
  /** Utility function: decodes a number from a given base. */
  FRM_BASE(UTILURI, FNUtil.class, 2, 2, "integer-from-base(str,base)", ITR),
  /** Utility function: calculates the MD5 hash of the given string. */
  MD5(UTILURI, FNUtil.class, 1, 1, "md5(str)", STR),
  /** Utility function: calculates the SHA1 hash of the given string. */
  SHA1(UTILURI, FNUtil.class, 1, 1, "sha1(str)", STR),
  /** Utility function: calculates the CRC32 hash of the given string. */
  CRC32(UTILURI, FNUtil.class, 1, 1, "crc32(str)", STR),
  /** Utility function: gets the bytes from the given base64 data. */
  TO_BYTES(UTILURI, FNUtil.class, 1, 1, "to-bytes(base64)", BYT_ZM),

  /* FNSent functions. */

  /** Sentiment function: returns a text sentiment. */
  SENT(SENTURI, FNSent.class, 2, 2, "polarity(string,uri)", ITR),
  /** Sentiment function: returns a normed polarity value. */
  NORMSENT(SENTURI, FNSent.class, 2, 2, "normed-polarity(string,uri)", ITR);

  /** Function classes. */
  final Class<? extends Fun> func;
  /** Function uri. */
  final byte[] uri;
  /** Descriptions. */
  final String desc;
  /** Minimum number of arguments. */
  final int min;
  /** Maximum number of arguments. */
  final int max;
  /** Return type. */
  final SeqType ret;

  /**
   * Constructor.
   * @param ur uri
   * @param fun function class
   * @param mn minimum number of arguments
   * @param mx maximum number of arguments
   * @param dsc description
   * @param rt return value
   */
  private FunDef(final byte[] ur, final Class<? extends Fun> fun, final int mn,
      final int mx, final String dsc, final SeqType rt) {
    uri = ur;
    func = fun;
    min = mn;
    max = mx;
    desc = dsc;
    ret = rt;
  }

  /**
   * Creates a new instance of the function.
   * @param ii input info
   * @param e expression array
   * @return function
   */
  public Fun get(final InputInfo ii, final Expr... e) {
    return (Fun) Reflect.get(Reflect.find(func, InputInfo.class, FunDef.class,
        Expr[].class), ii, this, e);
  }

  @Override
  public final String toString() {
    return desc;
  }
}
