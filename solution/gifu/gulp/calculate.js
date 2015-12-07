'use strict';





let result;

const

  TOPLEFT = 0,

  TOP = 1,

  TOPRIGHT = 2,

  BOTTOMRIGHT = 3,

  BOTTOM = 4,

  BOTTOMLEFT = 5,

  order = [0, 2, 1, 3, 5, 7, 4, 6, 8],

  //           Body Head
  // Punainen: 606,  60 
  // Vihreä:   515, 151
  // Musta:    424, 242
  pieces = {
    P1: [151, 0, 515, 0,  60, 0],
    P2: [ 60, 0, 515, 0, 424, 0],
    P3: [ 60, 0, 515, 0, 151, 0],
    P4: [ 60, 0, 606, 0, 515, 0],
    P5: [ 60, 0, 424, 0, 606, 0],
    P6: [424, 0, 515, 0, 242, 0],
    P7: [515, 0, 242, 0, 151, 0],
    P8: [242, 0,  60, 0, 424, 0],
    P9: [515, 0, 606, 0,  60, 0]
  },


  removePiece = (pieces, removeKey) => {
    let piecesLeft = {};
    for (let pieceKey in pieces) {
      if (pieceKey != removeKey) {
        piecesLeft[pieceKey] = pieces[pieceKey];
      }
    }
    return piecesLeft;
  },


  getValue = (piece, angle, side) => {
    return piece[(angle + side) % 6];
  },


  condition = (existing, existingSide, side) => {
    let value = getValue(pieces[existing[0]], existing[1], existingSide);
    return {value, side};
  },


  resolveConditions = (index, combination) => {
    switch (order[index]) {
      case 1: return [condition(combination[2], BOTTOMLEFT, TOPRIGHT)];
      case 2: return [condition(combination[0], BOTTOM, TOP)];
      case 3: return [condition(combination[2], BOTTOMRIGHT, TOPLEFT)];
      case 4: return [condition(combination[5], BOTTOMLEFT, TOPRIGHT)];
      case 5: return [condition(combination[1], BOTTOM, TOP)];
      case 6: return [condition(combination[5], BOTTOMRIGHT, TOPLEFT), condition(combination[7], BOTTOMLEFT, TOPRIGHT)];
      case 7: return [condition(combination[3], BOTTOM, TOP)];
      case 8: return [condition(combination[7], BOTTOMRIGHT, TOPLEFT)];
    }
  },


  findNextPiece = (index, piecesLeft, combination) => {

    let conditions = resolveConditions(index, combination);

    for (let pieceKey in piecesLeft) {

      let piece = piecesLeft[pieceKey];

      for (let angle = 0; angle < 6; angle++) {

        let matches = conditions.reduce((matches, condition) => {
          let value = getValue(piece, angle, condition.side);
          if (value + condition.value == 666) {
            return matches;
          }
          return false;
        }, true);

        if (matches) {

          let
            newPiecesLeft = removePiece(piecesLeft, pieceKey),
            newCombination = combination.slice();

          newCombination[order[index]] = [pieceKey, angle];

          if (newCombination.length == 9) {
            result.push(newCombination);
          } else {
            findNextPiece(index + 1, newPiecesLeft, newCombination);
          }

        }

      }
    }

  },


  calculate = () => {

    result = [];

    for (let pieceKey in pieces) {
      let piecesLeft = removePiece(pieces, pieceKey);
      for (let angle = 0; angle < 6; angle += 2) {
        findNextPiece(1, piecesLeft, [[pieceKey, angle]]);
      }
    }

    return result;

  };





module.exports = calculate;
