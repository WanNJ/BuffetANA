/**
 * Created by slow_time on 2017/5/16.
 */
let strategyTool = require('../../../bl/strategy/strategyTool');
let describe = require("mocha").describe;
let it = require("mocha").it;

let expect = require('chai').expect;


describe('#compareTo()', function () {
    it('比较两个策略是否相同', function () {
        let a = { _id: '59417315f16251c4deb50919',
            beginDate: '2015-12-31T16:00:00.000Z',
            endDate: '2017-04-27T16:00:00.000Z',
            stockPoolConditionVO:
        { excludeST: true,
            industries: null,
            benches: null,
            stockPool: '沪深300' },
        rank:
        { rank1: [ 'KDJ_D', 'asd', 4, 1 ],
            rank2: [ 'MA', 'asd', 10, 3 ],
            rank3: [ 'MOM', 'asd', 5, 1 ] },
        filter: { filter1: [ 'turnOverRate', '>', 1 ] },
        tradeModelVO: { loseRate: null, winRate: null, holdingNums: 5, holdingDays: 6 },
        envSpecDay: 3,
            markNormal:
        { profitAbility: 5,
            stability: 19,
            chooseStockAbility: 12,
            absoluteProfit: 1,
            antiRiskAbility: 19,
            strategyScore: 56 },
        markHS:
        { profitAbility: 8,
            stability: 19,
            chooseStockAbility: 12,
            absoluteProfit: 1,
            antiRiskAbility: 19,
            strategyScore: 59 },
        markHO:
        { profitAbility: 20,
            stability: 19,
            chooseStockAbility: 14,
            absoluteProfit: 4,
            antiRiskAbility: 20,
            strategyScore: 77 },
        markLS:
        { profitAbility: 4,
            stability: 15,
            chooseStockAbility: 10,
            absoluteProfit: 0,
            antiRiskAbility: 17,
            strategyScore: 46 },
        markLO:
        { profitAbility: 20,
            stability: 19,
            chooseStockAbility: 10,
            absoluteProfit: 1,
            antiRiskAbility: 20,
            strategyScore: 70 } };
        let b = { beginDate: '2015-12-31T16:00:00.000Z',
            endDate: '2017-04-27T16:00:00.000Z',
            stockPoolConditionVO:
        { excludeST: true,
            benches: null,
            industries: null,
            stockPool: '沪深300' },
        rank:
        { rank1: [ 'KDJ_D', 'asd', 4, 1 ],
            rank2: [ 'MA', 'asd', 10, 3 ],
            rank3: [ 'MOM', 'asd', 5, 1 ] },
        filter: { filter1: [ 'turnOverRate', '>', 1 ] },
        tradeModelVO: { loseRate: null, winRate: null, holdingNums: 5, holdingDays: 6 },
        envSpecDay: 3,
            markNormal:
        { profitAbility: 5,
            stability: 19,
            chooseStockAbility: 12,
            absoluteProfit: 1,
            antiRiskAbility: 19,
            strategyScore: 56 },
        markHS:
        { profitAbility: 8,
            stability: 19,
            chooseStockAbility: 12,
            absoluteProfit: 1,
            antiRiskAbility: 19,
            strategyScore: 59 },
        markHO:
        { profitAbility: 20,
            stability: 19,
            chooseStockAbility: 14,
            absoluteProfit: 4,
            antiRiskAbility: 20,
            strategyScore: 77 },
        markLS:
        { profitAbility: 4,
            stability: 15,
            chooseStockAbility: 10,
            absoluteProfit: 0,
            antiRiskAbility: 17,
            strategyScore: 46 },
        markLO:
        { profitAbility: 20,
            stability: 19,
            chooseStockAbility: 10,
            absoluteProfit: 1,
            antiRiskAbility: 20,
            strategyScore: 70 } };

        let isSame = strategyTool.compareTo(a, b);
        expect(isSame).to.be.equal(true);
    });
});