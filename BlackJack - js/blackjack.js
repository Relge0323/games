var dealerSum = 0;
var yourSum = 0;

var dealerAceCount = 0;
var yourAceCount = 0;

var hidden;
var deck;

var canHit = true;  // allows the player to draw yourSum <= 21

window.onload = function() {
    buildDeck();
    shuffleDeck();
    startGame();

    // Add event listeners to the buttons
    document.getElementById("hit").addEventListener("click", hit);
    document.getElementById("stay").addEventListener("click", stay);
}

function buildDeck() {
    let ranks = ['A', '2', '3', '4', '5', '6', '7', '8', '9', '10', 'J', 'Q', 'K'];
    let suits = ['C', 'D', 'H', 'S'];
    deck = [];

    for (let i = 0; i < suits.length; i++) {
        for (let j = 0; j < ranks.length; j++) {
            deck.push(ranks[j] + "-" + suits[i]);  // add the card to the deck
        }
    }
}

function shuffleDeck() {
    for (let i  = 0; i < deck.length; i++) {
        let j = Math.floor(Math.random() * deck.length);
        let temp = deck[i];
        deck[i] = deck[j];
        deck[j] = temp;
    }
    console.log(deck);
}

function startGame() {
    // Dealer's hidden card (not shown yet)
    hidden = deck.pop();
    
    // Show dealer's visible card
    let dealerCardImg = document.createElement("img");
    let dealerCard = deck.pop();
    dealerCardImg.src = "./cards/" + dealerCard + ".png";
    document.getElementById("dealer-cards").append(dealerCardImg);
    dealerSum += getValue(dealerCard);
    dealerAceCount += checkAce(dealerCard);

    // Add two cards to player's hand
    for (let i = 0; i < 2; i++) {
        let cardImg = document.createElement("img");
        let card = deck.pop();
        cardImg.src = "./cards/" + card + ".png";
        document.getElementById("your-cards").append(cardImg);
        yourSum += getValue(card);
        yourAceCount += checkAce(card);
    }

    console.log("your sum:", yourSum);
    console.log("dealer showing:", dealerSum);
}

function hit() {
    if (!canHit) {
        return;
    }
    let cardImg = document.createElement("img");
    let card = deck.pop();
    cardImg.src = "./cards/" + card + ".png";
    yourSum += getValue(card);
    yourAceCount += checkAce(card);
    document.getElementById("your-cards").append(cardImg);

    if (reduceAce(yourSum, yourAceCount) > 21) {
        canHit = false;
    }
}

function stay() {
    // Reveal the hidden card
    dealerSum += getValue(hidden);  // Add hidden card value to dealer's sum
    dealerAceCount += checkAce(hidden);  // Add Ace check
    dealerSum = reduceAce(dealerSum, dealerAceCount);  // Reduce Ace if needed

    yourSum = reduceAce(yourSum, yourAceCount);  // Adjust player's sum for Aces

    canHit = false;
    // Reveal the hidden card
    document.getElementById("hidden").src = "./cards/" + hidden + ".png";

    // Dealer's turn (keep hitting until dealer sum is 17 or higher)
    while (dealerSum < 17) {
        let cardImg = document.createElement("img");
        let card = deck.pop();
        cardImg.src = "./cards/" + card + ".png";
        document.getElementById("dealer-cards").append(cardImg);

        dealerSum += getValue(card);
        dealerAceCount += checkAce(card);
        dealerSum = reduceAce(dealerSum, dealerAceCount);  // Adjust for Aces if needed
    }

    let message = "";
    if (yourSum > 21) {
        message = "You Lose!";
    } else if (dealerSum > 21) {
        message = "You Win!";
    } else if (yourSum == dealerSum) {
        message = "Tie!";
    } else if (yourSum > dealerSum) {
        message = "You Win!";
    } else if (yourSum < dealerSum) {
        message = "You Lose!";
    }

    document.getElementById("dealer-sum").innerText = dealerSum;
    document.getElementById("your-sum").innerText = yourSum;
    document.getElementById("results").innerText = message;
}


function getValue(card) {
    let data = card.split("-");     // "4-c" -> ["4", "c"]
    let value = data[0];

    if (isNaN(value)) { //A J Q K
        if (value == "A") {
            return 11;
        }
        return 10;
    }
    return parseInt(value);
}

function checkAce(card) {
    if (card[0] == "A") {
        return 1;
    }
    return 0;
}

function reduceAce(playerSum, playerAceCount) {
    while (playerSum > 21 && playerAceCount > 0) {
        playerSum -= 10;
        playerAceCount -= 1;
    }
    return playerSum;
}
